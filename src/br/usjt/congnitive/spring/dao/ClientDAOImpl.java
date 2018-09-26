package br.usjt.congnitive.spring.dao;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.usjt.congnitive.spring.model.Client;

public class ClientDAOImpl implements ClientDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	@Override
	public void addClient(Client c) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(c);
		logger.info("Client saved successfully, Client Details=" + c);
		
	}

	@Override
	public void updateClient(Client c) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(c);
		logger.info("CLient updated successfully, Client Details=" + c);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Client> listClients() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Client> clientsList = session.createQuery("from Client").list();
		for(Client c : clientsList) {
			logger.error("Client List::"+ c);
		}
		return clientsList;
	}

	@Override
	public Client getClientById(int id) {
		Session session = this.sessionFactory.getCurrentSession();		
		Client c = (Client) session.load(Client.class, new Integer(id));
		logger.info("Client loaded successfully, Client details=" + c );
		return c;
	}

	@Override
	public void removeClient(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Client c = (Client) session.load(Client.class, new Integer(id));
		if(null != c){
			session.delete(c);
		}
		logger.info("Client deleted successfully, Client details=" + c);
	}
}
