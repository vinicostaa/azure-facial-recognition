package br.usjt.congnitive.spring.service;

import java.util.List;

import br.usjt.congnitive.spring.model.Client;

public interface ClientService {
	public void addClient(Client p);
	public void updateClient(Client p);
	public List<Client> listClients();
	public Client getClientById(int id);
	public void removeClient(int id);
}
