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
	
	/**
	 * Busca a face do cliente atrávez do id
	 * @param id - id da face a ser buscada
	 * @return Retorna a face correspondente ao id
	 * @author Grupo2
	 */
	@Override
	public Face getFaceById(int id) {
		Session session = this.sessionFactory.getCurrentSession();		
		Face f = (Face) session.load(Face.class, new Integer(id));
		logger.info("Face loaded successfully, Face details=" + f );
		return f;
	}

	/**
	 * Busca Face atrávez do id do cliente
	 * @param id - id do cliente 
	 * @return Retorna a face do cliente
	 */
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
