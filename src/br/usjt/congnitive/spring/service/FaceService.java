package br.usjt.congnitive.spring.service;

import java.io.File;
import java.io.InputStream;

public interface FaceService {
	public String Detect(File imageStream, boolean returnFaceId, boolean returnFaceLandmarks);

	public String FindSimilar(String faceId, String faceListId, String largeFaceListId, int maxNumOfCandidatesReturned);

	public String AddFaceToFaceList(int faceListId, File imageStream, String userData);
	
}
