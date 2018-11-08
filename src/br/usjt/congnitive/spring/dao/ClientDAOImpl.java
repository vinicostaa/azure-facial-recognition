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
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

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

	@Override
	public Face getClientsByPersonId(String personId) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Face where personId = :id ");
		query.setParameter("id", personId);
		List<Face> listFaces = query.list();
		if(listFaces.size() > 0) {
			return listFaces.get(0);
		} else {
			return null;
		}
	}
}
