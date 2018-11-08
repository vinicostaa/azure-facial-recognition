package br.usjt.congnitive.spring.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import br.usjt.congnitive.spring.model.Candidates;
import br.usjt.congnitive.spring.model.Client;
import br.usjt.congnitive.spring.model.Face;
import br.usjt.congnitive.spring.model.PersistedFaceIds;
import br.usjt.congnitive.spring.service.ClientService;
import br.usjt.congnitive.spring.service.FaceService;

@RestController
public class ClientController {

	private ClientService clientService;
	private FaceService faceService;
	private List<Face> facesBanco;

	@Autowired(required = true)
	@Qualifier(value = "clientService")
	public void setClientService(ClientService cs) {
		this.clientService = cs;
	}

	@Autowired(required = true)
	@Qualifier(value = "faceService")
	public void setFaceService(FaceService fs) {
		this.faceService = fs;
	}

	@RequestMapping(value = "/client/list", method = RequestMethod.GET)
	public @ResponseBody List<Client> listClients() {
		List<Client> clients = this.clientService.listClients();
		for (Client client : clients) {
			Set<Face> facesBanco = new HashSet<Face>();
			Face face = this.faceService.getFaceByClientId(client.getId());
			String[] imagesPaths = face.getPath().split("\\|");
			ArrayList<String> bases64images = new ArrayList<String>();
			for (String imagePath: imagesPaths) {
				String base64 = "";
				try {
					base64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(Files.readAllBytes(Paths.get(imagePath)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bases64images.add(base64);
				face.setBase64image(bases64images);
				facesBanco.add(face);
				client.setFace(facesBanco);
			}
		}
		return clients;
	}
//
//	//For add and update client both
//	@RequestMapping(value= "/client/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public String addClient(@RequestBody String json){
//		
//		Client c = new Gson().fromJson(json, Client.class); 
//		Face f = new Face();
//
//        Iterator<Face> carrosAsIterator = c.getFace().iterator();
//        while (carrosAsIterator.hasNext()){
//               f = carrosAsIterator.next();
//        }
//        
//		String base64Image = f.getBase64image().split(",")[1];
//		
//		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
//		String path = "C:\\\\Projects\\\\faces-azure\\\\"+ c.getCpf() +".jpg";
//		Path destinationFile = Paths.get(path);
//		
//		try {
//			//salvando
//			Files.write(destinationFile, imageBytes);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		Face faceAzure = this.faceService.AddFaceToFaceList(new File(path), c.getNome());
//		f.setPersistedFaceId(faceAzure.getPersistedFaceId());
//		f.setCliente(c);
//		f.setPath(path);
//		
//		if(c.getId() == 0){
//			//new client, add it
//			this.clientService.addClientFace(c, f);
//		}else{
//			//existing client, call update
//			this.clientService.updateClient(c);
//		}
//		
//		return json;
//	}

	// For add and update client both
	@RequestMapping(value = "/client/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addClient(@RequestBody String json) {

		try {
			Client c = new Gson().fromJson(json, Client.class);
			Face f = new Face();
			PersistedFaceIds p = new PersistedFaceIds();

			Iterator<Face> faceAsIterator = c.getFace().iterator();
			while (faceAsIterator.hasNext()) {
				f = faceAsIterator.next();
			}

			
			Face faceCreate = this.faceService.CreatePersonGroup(c.getNome(), c.getEmail());
			
			Face faceAdd = new Face();
			String clientsPaths = "";
			
			try {
				// salvando
				int index = 0;
				for (String base64Image: f.getBase64image()) {
					index++;
					byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image.split(",")[1]);
					String path = "C:\\\\Projects\\\\faces-azure\\\\" + c.getCpf() + "-"+ index + ".jpg";
					clientsPaths += path + "|"; 
					Path destinationFile = Paths.get(path);
					Files.write(destinationFile, imageBytes);
					faceAdd = this.faceService.AddPersonFaceInPersonGroupAsync(faceCreate.getPersonId(), c.getNome(),
							new File(path));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			f.setPersonId(faceCreate.getPersonId());
			f.setName(c.getNome());
			f.setUserData(c.getEmail());
			f.setCliente(c);
			f.setPath(clientsPaths);

			p.setFace(f);
			p.setPersistedfaceid(faceAdd.getPersistedFaceId());

			if (c.getId() == 0) {
				// new client, add it
				this.clientService.addClientFace(c, f, p);
				this.faceService.train();
			} else {
				// existing client, call update
				this.clientService.updateClient(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return json;
	}

	@RequestMapping(value = "/client/detect", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Client> detectClient(@RequestBody String json) {
		Face f = new Gson().fromJson(json, Face.class);

		String base64Image = f.getBase64image().get(0).split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		String path = "C:\\Projects\\faces-azure\\myImage.jpg";
		Path destinationFile = Paths.get(path);

		try {
			Files.write(destinationFile, imageBytes);
			String faceId = this.faceService.Detect(new File(path), true, false);
			ArrayList<String> facesIds = new ArrayList<String>();
			facesIds.add(faceId);

			// Call Identify
			ArrayList<Face> faces = this.faceService.Identify(facesIds);

			// Chamar FindSimilar
//			List<Face> facesFindSimilar = this.faceService.FindSimilar(faceId, 10);
			List<Client> clients = new ArrayList<Client>();
//			
//			
			for (Face face : faces) {
				for (Candidates candidates : face.getCantidates()) {
					Set<Face> facesBanco = new HashSet<Face>();
					Face faceBanco = new Face();
					faceBanco = this.clientService.getClientsByPersonId(candidates.getPersonId());
					if(faceBanco == null) {
						continue;
					}
					ArrayList<Candidates> listaCandidates = new ArrayList<>();
					listaCandidates.add(candidates);
					faceBanco.setCantidates(listaCandidates);
					//faceBanco.setCantidates(face.getCantidates());
//					faceBanco.setConfidence(face.getConfidence());
					String[] imagesPaths = faceBanco.getPath().split("\\|");
					ArrayList<String> bases64images = new ArrayList<String>();
					for (String imagePath: imagesPaths) {
						String base64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(Files.readAllBytes(Paths.get(imagePath)));
						bases64images.add(base64);
						faceBanco.setBase64image(bases64images);
						facesBanco.add(faceBanco);
					}
					
					Client client = this.clientService.getClientById(faceBanco.getCliente().getId());
					client.setFace(facesBanco);
					clients.add(client);
				}

			}
			
 			return clients;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("/remove/{id}")
	public void removeClient(@PathVariable("id") int id) {

		this.clientService.removeClient(id);
		// return "redirect:/clients";
	}

	@RequestMapping("/edit/{id}")
	public void editClient(@PathVariable("id") int id, Model model) {
		model.addAttribute("client", this.clientService.getClientById(id));
		model.addAttribute("listClients", this.clientService.listClients());
		// return "client";
	}

}
