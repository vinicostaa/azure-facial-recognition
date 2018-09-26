package br.usjt.congnitive.spring.service;

import java.util.List;

import javax.transaction.Transactional;

import br.usjt.congnitive.spring.dao.ClientDAO;
import br.usjt.congnitive.spring.model.Client;

public class ClientServiceImpl implements ClientService {

	private ClientDAO clientDAO;

	public void setClientDAO(ClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}
	
	@Override
	@Transactional
	public void addClient(Client c) {
		this.clientDAO.addClient(c);
	}
	
	@Override
	@Transactional
	public void updateClient(Client c) {
		this.clientDAO.updateClient(c);
	}

	
	@Override
	@Transactional
	public List<Client> listClients() {
		return this.clientDAO.listClients();
	}

	@Override
	@Transactional
	public Client getClientById(int id) {
		return this.clientDAO.getClientById(id);
	}

	@Override
	@Transactional
	public void removeClient(int id) {
		this.clientDAO.removeClient(id);
	}
}
