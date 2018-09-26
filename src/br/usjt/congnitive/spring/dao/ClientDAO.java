package br.usjt.congnitive.spring.dao;

import java.util.List;

import br.usjt.congnitive.spring.model.Client;

public interface ClientDAO {
	
	public void addClient(Client c);
	public void updateClient(Client c);
	public List<Client> listClients();
	public Client getClientById(int id);
	public void removeClient(int id);

}
