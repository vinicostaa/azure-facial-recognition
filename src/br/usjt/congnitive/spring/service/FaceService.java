package br.usjt.congnitive.spring.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.usjt.congnitive.spring.model.Client;
import br.usjt.congnitive.spring.model.Face;

public interface FaceService {
	public String Detect(File imageStream, boolean returnFaceId, boolean returnFaceLandmarks);

	public List<Face> FindSimilar(String faceId, int maxNumOfCandidatesReturned);

	public Face AddFaceToFaceList( File imageStream, String userData);
	
	public Face getFaceById(int id);

	public Face getFaceByClientId(int id);

	public Face AddPersonFaceInPersonGroupAsync(String personId, String userData, File imageStream);

	public ArrayList<Face> Identify(ArrayList<String> faceIds);

	public Face CreatePersonGroup(String name, String userData);
	
	public boolean train();
}
