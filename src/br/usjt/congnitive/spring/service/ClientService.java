package br.usjt.congnitive.spring.service;

import java.util.List;

import br.usjt.congnitive.spring.model.Client;
import br.usjt.congnitive.spring.model.Face;
import br.usjt.congnitive.spring.model.PersistedFaceIds;

public interface ClientService {
	public void addClientFace(Client c, Face f, PersistedFaceIds p);
	
	public void updateClient(Client p);
	
	public List<Client> listClients();
	
	public Client getClientById(int id);
	
	public void removeClient(int id);
	
	public Face getClientsByPersonId(String persistedFaceId);
	
}
