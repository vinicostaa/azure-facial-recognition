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

/**
 * Classe para objetos do tipo Cliente Controller, onde será contidos, valores,
 * métodos, construtores e atributos.
 * 
 * @param ClientService
 * @param FaceService
 * @param List<Face>
 * @autor Grupo 2
 */
@RestController
public class ClientController {

	private ClientService clientService;
	private FaceService faceService;

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

	/**
	 * Método para listar o cliente encontrado no banco de dados MySQL.
	 * 
	 * @param listclientes - listar clintes do banco.
	 * @see Client.java - está referenciando a classe Client da model.
	 * @see ClientService.java - está referenciando a classe ClientService da
	 *      service.
	 * @return retorna os dados do cliente encontrado no banco de dados.
	 * @autor Grupo 2
	 * @throw IOException - informar se o método recebe erros de entrada e saida na
	 *        leitura das imagens.
	 */
	@RequestMapping(value = "/client/list", method = RequestMethod.GET)
	public @ResponseBody List<Client> listClients() {
		System.out.println(System.getProperty("catalina.base"));
		List<Client> clients = this.clientService.listClients();
		for (Client client : clients) {
			Set<Face> facesBanco = new HashSet<Face>();
			Face face = this.faceService.getFaceByClientId(client.getId());
			String[] imagesPaths = face.getPath().split("\\|");
			ArrayList<String> bases64images = new ArrayList<String>();
			for (String imagePath : imagesPaths) {
				String base64 = "";
				try {
					base64 = "data:image/png;base64,"
							+ DatatypeConverter.printBase64Binary(Files.readAllBytes(Paths.get(imagePath)));
				} catch (IOException e) {
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

	/**
	 * Método para adicionar ou atualizar o cliente.
	 * 
	 * @param addClient    - adicionar cliente
	 * @param updateClient - atualizar o cliente
	 * @see Client.java - está referenciando a classe cliente da model.
	 * @return Json para converter o objeto para o formato adequado antes de retorna-lo
	 * @autor Grupo 2
	 */
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
				int index = 0;
				for (String base64Image : f.getBase64image()) {
					index++;
					byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image.split(",")[1]);
					String path = System.getProperty("catalina.base") + File.separator + c.getCpf() + "-" + index
							+ ".jpg";
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

	/**
	 * Método para detectar o cliente
	 * 
	 * @param detectClient - detectar o cliente atrávez de uma foto.
	 * @see Client.java - está referenciando a classe cliente da model.
	 * @return null - caso o cliente não seja detectado
	 * @return clients - retorna o cliente detectado
	 * @autor Grupo 2
	 * @throw IOException - informar se o método recebe erros de entrada e saida na
	 *        leitura das imagens.
	 */
	@RequestMapping(value = "/client/detect", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Client> detectClient(@RequestBody String json) {
		Face f = new Gson().fromJson(json, Face.class);

		String base64Image = f.getBase64image().get(0).split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		String path = System.getProperty("catalina.base") + File.separator + "myImage.jpg";
		Path destinationFile = Paths.get(path);

		try {
			Files.write(destinationFile, imageBytes);
			String faceId = this.faceService.Detect(new File(path), true, false);
			ArrayList<String> facesIds = new ArrayList<String>();
			facesIds.add(faceId);
			ArrayList<Face> faces = this.faceService.Identify(facesIds);
			List<Client> clients = new ArrayList<Client>();

			for (Face face : faces) {
				for (Candidates candidates : face.getCantidates()) {
					Set<Face> facesBanco = new HashSet<Face>();
					Face faceBanco = new Face();
					faceBanco = this.clientService.getClientsByPersonId(candidates.getPersonId());
					if (faceBanco == null) {
						continue;
					}
					ArrayList<Candidates> listaCandidates = new ArrayList<>();
					listaCandidates.add(candidates);
					faceBanco.setCantidates(listaCandidates);
					String[] imagesPaths = faceBanco.getPath().split("\\|");
					ArrayList<String> bases64images = new ArrayList<String>();
					for (String imagePath : imagesPaths) {
						String base64 = "data:image/png;base64,"
								+ DatatypeConverter.printBase64Binary(Files.readAllBytes(Paths.get(imagePath)));
						bases64images.add(base64);
						faceBanco.setBase64image(bases64images);
						facesBanco.add(faceBanco);
					}
					Client client = this.clientService.getClientById(faceBanco.getCliente().getId());
					client.setFace(facesBanco);
					clients.add(client);
				}
			}

			if (clients.size() <= 0) {
				return null;
			}

			return clients;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Método para remover o cliente
	 * 
	 * @param removeClient - remover o cliente
	 * @see ClientSerice.java - está referenciando a classe cliente service da
	 *      service.
	 * @autor Grupo 2
	 */
	@RequestMapping("/remove/{id}")
	public void removeClient(@PathVariable("id") int id) {
		this.clientService.removeClient(id);
	}

	@RequestMapping("/edit/{id}")
	public void editClient(@PathVariable("id") int id, Model model) {
		model.addAttribute("client", this.clientService.getClientById(id));
		model.addAttribute("listClients", this.clientService.listClients());
	}

}
