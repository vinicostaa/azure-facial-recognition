package br.usjt.congnitive.spring.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import br.usjt.congnitive.spring.model.Client;
import br.usjt.congnitive.spring.model.Face;
import br.usjt.congnitive.spring.service.ClientService;
import br.usjt.congnitive.spring.service.FaceService;

@RestController
public class ClientController {
	
	private ClientService clientService;
	private FaceService faceService;
	
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String Home() {
		
		this.faceService.Detect(new File("C:\\\\Projects\\\\faces-azure\\\\menina.jpg"), true, false);
		
		
		return "home";
	}

	@Autowired(required=true)
	@Qualifier(value="clientService")
	public void setClientService(ClientService cs){
		this.clientService = cs;
	}
	
	@Autowired(required=true)
	@Qualifier(value="faceService")
	public void setFaceService(FaceService fs){
		this.faceService = fs;
	}
	
	@RequestMapping(value = "/clients", method = RequestMethod.GET)
	public void listClients(Model model) {
		model.addAttribute("client", new Client());
		model.addAttribute("listClients", this.clientService.listClients());
		//return "client";
	}
	
	
	@RequestMapping(value = "/curriculo-test", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String teste(@RequestBody String json) {
		
		Gson gson = new Gson(); // Or use new GsonBuilder().create();
		Face target2 = gson.fromJson(json, Face.class); // deserializes json into target2

        return null;
    }

	
	
	//For add and update client both
	@RequestMapping(value= "/client/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addClient(@RequestBody String json){
		
		Gson gson = new Gson(); // Or use new GsonBuilder().create();
		Client c = gson.fromJson(json, Client.class); // deserializes json into target2
		
//		String base64Image = c.getFace().getBase64image().split(",")[1];
//		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
//		Path destinationFile = Paths.get("C:\\Projects\\faces-azure", "myImage2.jpg");
//		try {
//			Files.write(destinationFile, imageBytes);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		c.setCpf("44539668");
//		c.setEmail("joao@gmail.com");
//		c.setNome("Jonas Macedo");
//		c.setRg("348602349");
//		c.setTelefone("119404");
//		
//		this.faceService.AddFaceToFaceList(1, new File("C:\\\\Projects\\\\faces-azure\\\\myImage2.jpg"), c.getNome());
//		
//		c.setCpf("44539668");
//		c.setEmail("joao@gmail.com");
//		c.setNome("Jonas Macedo");
//		c.setRg("348602349");
//		c.setTelefone("119404");
		
		
		if(c.getId() == 0){
			//new client, add it
			this.clientService.addClient(c);
		}else{
			//existing client, call update
			this.clientService.updateClient(c);
		}
		
		return json;
		
		//return "redirect:/clients";
		
	}
	
	
	@RequestMapping(value= "/client/detect", method = RequestMethod.POST)
	public void detectClient(@ModelAttribute("face") Face f){
		
		String base64Image = f.getBase64image().split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		
		
		
		Path destinationFile = Paths.get("C:\\Projects\\faces-azure", "myImage2.jpg");
		try {
			Files.write(destinationFile, imageBytes);	
			this.faceService.AddFaceToFaceList(1, new File("C:\\\\Projects\\\\faces-azure\\\\myImage2.jpg"), "imagemTeste");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("/remove/{id}")
    public void removeClient(@PathVariable("id") int id){
		
        this.clientService.removeClient(id);
        //return "redirect:/clients";
    }
 
    @RequestMapping("/edit/{id}")
    public void editClient(@PathVariable("id") int id, Model model){
        model.addAttribute("client", this.clientService.getClientById(id));
        model.addAttribute("listClients", this.clientService.listClients());
        //return "client";
    }
    
}
