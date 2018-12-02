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
	
	/**
	 * Persistir um cliente no banco de dados.
	 * @param c - Objeto Cliente a ser persistido
	 * @param f - Objeto Face a ser persistido.
	 * @param p - Objeto PersistedFaceIds a ser persistido.
	 * @author Grupo 2
	 */
	@Override
	@Transactional
	public void addClientFace(Client c, Face f, PersistedFaceIds p) {
		this.clientDAO.addClientFace(c, f, p);
	}
	
	/**
	 * Atualiza um cliente na base
	 * @param c - Objeto Cliente a ser persistido
	 * @author Grupo 2
	 */
	@Override
	@Transactional
	public void updateClient(Client c) {
		this.clientDAO.updateClient(c);
	}

	/**
	 * Lista todos os clientes cadastrados na base de dados.
	 * @return Lista de clientes cadastrados de dados.
	 * @author Grupo 2
	 */
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

	/**
	 * Deleta o Cliente pelo seu Id
	 * @author Grupo 2
	 */
	@Override
	@Transactional
	public void removeClient(int id) {
		this.clientDAO.removeClient(id);
	}

	/**
	 * Busca o Face pela seu PersonID
	 * @return Face com o PersonId passado por parâmetro
	 * @author Grupo 2
	 */
	@Override
	@Transactional
	public Face getClientsByPersonId(String personId) {
		return this.clientDAO.getClientsByPersonId(personId);
	}
}
