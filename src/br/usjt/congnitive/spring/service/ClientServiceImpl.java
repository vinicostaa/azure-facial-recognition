package br.usjt.congnitive.spring.service;

import java.util.List;

import javax.transaction.Transactional;

import br.usjt.congnitive.spring.dao.ClientDAO;
import br.usjt.congnitive.spring.model.Client;
import br.usjt.congnitive.spring.model.Face;
import br.usjt.congnitive.spring.model.PersistedFaceIds;

public class ClientServiceImpl implements ClientService {

	private ClientDAO clientDAO;

	public void setClientDAO(ClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}
	
	@Override
	@Transactional
	public void addClientFace(Client c, Face f, PersistedFaceIds p) {
		this.clientDAO.addClientFace(c, f, p);
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

	@Override
	@Transactional
	public Face getClientsByPersonId(String personId) {
		return this.clientDAO.getClientsByPersonId(personId);
	}
}
