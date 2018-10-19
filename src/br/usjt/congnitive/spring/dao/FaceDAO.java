package br.usjt.congnitive.spring.dao;

import br.usjt.congnitive.spring.model.Face;

public interface FaceDAO {

	public Face getFaceById(int id);
	
	public Face getFaceByClientId(int id);
}
