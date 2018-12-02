package br.usjt.congnitive.spring.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.usjt.congnitive.spring.model.Client;
import br.usjt.congnitive.spring.model.Face;
import br.usjt.congnitive.spring.model.PersistedFaceIds;

public class ClientDAOImpl implements ClientDAO {

	private static final Logger logger = LoggerFactory.getLogger(ClientDAOImpl.class);

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	/**
	 * Metodo responsável por inserir um cliente no banco de dados.
	 * 
	 * @param c representa o objeto Cliente que será persistido.
	 * @param f representa o objeto Face que será persistido
	 * @param p representa o objeto PersistedFaceIds que será persistido
	 * @author Grupo2
	 */
	@Override
	public void addClientFace(Client c, Face f, PersistedFaceIds p) {
		Session session = this.sessionFactory.getCurrentSession();

		session.save(c);
		session.save(f);
		session.save(p);

		System.out.println("Client saved successfully, Client Details=" + c);
		System.out.println("Face saved successfully, Face Details=" + f);
		System.out.println("PersistedFaceIds saved successfully, PersistedFaceIds Details=" + p);
	}

	/**
	 * Atualiza os dados do cliente
	 * @param c Referencia os dados do usuario
	 * @author Grupo2
	 */
	@Override
	public void updateClient(Client c) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(c);
		logger.info("CLient updated successfully, Client Details=" + c);
	}

	/**
	 * Armazena os clientes cadastrados em um arraylist
	 * @return listClients - Lista de clientes
	 * @author Grupo2
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Client> listClients() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Client> clientsList = session.createQuery("from Client").list();
		for (Client c : clientsList) {
			logger.error("Client List::" + c);
		}
		return clientsList;
	}

	/**
	 * Retorna dados do cliente de acordo com o id
	 * @param id - id do cliente a ser buscado
	 * @return id - cliente com o id referenciado
	 * @author Grupo2
	 */
	@Override
	public Client getClientById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Client c = (Client) session.load(Client.class, new Integer(id));
		logger.info("Client loaded successfully, Client details=" + c);
		return c;
	}
	
	/**
	 * Remove cliente referente ao id
	 * @param id - id do cliente a ser removido
	 * @author Grupo2
	 */
	@Override
	public void removeClient(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Client c = (Client) session.load(Client.class, new Integer(id));
		if (null != c) {
			session.delete(c);
		}
		logger.info("Client deleted successfully, Client details=" + c);
	}

	/**
	 * Busca cliente de acordo com seu personId
	 * @param personId - PersonId a ser buscado
	 * @return personId - Retorna uma lista de faces cadastradas.
	 * @author Grupo2
	 */
	@Override
	public Face getClientsByPersonId(String personId) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Face where personId = :id ");
		query.setParameter("id", personId);
		List<Face> listFaces = query.list();
		if (listFaces.size() > 0) {
			return listFaces.get(0);
		} else {
			return null;
		}
	}
}
