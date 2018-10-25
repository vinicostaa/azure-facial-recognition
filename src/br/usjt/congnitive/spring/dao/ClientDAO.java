package br.usjt.congnitive.spring.dao;

import java.util.List;

import br.usjt.congnitive.spring.model.Client;
import br.usjt.congnitive.spring.model.Face;
import br.usjt.congnitive.spring.model.PersistedFaceIds;

public interface ClientDAO {
	
	public void addClientFace(Client c, Face f, PersistedFaceIds p);
	
	public void updateClient(Client c);
	
	public List<Client> listClients();
	
	public Client getClientById(int id);
	
	public void removeClient(int id);
	
	public Face getClientsByPersonId(String personId);

}
