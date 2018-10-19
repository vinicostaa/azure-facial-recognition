package br.usjt.congnitive.spring.service;

import java.util.List;

import br.usjt.congnitive.spring.model.Client;
import br.usjt.congnitive.spring.model.Face;

public interface ClientService {
	public void addClientFace(Client p, Face f);
	
	public void updateClient(Client p);
	
	public List<Client> listClients();
	
	public Client getClientById(int id);
	
	public void removeClient(int id);
	
	public Face getClientsByPersistedFaceId(String persistedFaceId);
	
}
