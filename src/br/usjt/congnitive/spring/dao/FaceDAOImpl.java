package br.usjt.congnitive.spring.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.usjt.congnitive.spring.model.Face;

public class FaceDAOImpl implements FaceDAO {

	private static final Logger logger = LoggerFactory.getLogger(ClientDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}
	
	@Override
	public Face getFaceById(int id) {
		Session session = this.sessionFactory.getCurrentSession();		
		Face f = (Face) session.load(Face.class, new Integer(id));
		logger.info("Face loaded successfully, Face details=" + f );
		return f;
	}

	@Override
	public Face getFaceByClientId(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Face where cliente.id = :id ");
		query.setParameter("id", id);
		List<Face> listFaces = query.list();
		if(listFaces.size() > 0) {
			return listFaces.get(0);
		} else {
			return null;
		}
	}

}
