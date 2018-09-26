package br.usjt.congnitive.spring.service;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class FaceServiceImpl implements FaceService {
	
	private final String DEFAULT_API_ROOT = "https://brazilsouth.api.cognitive.microsoft.com/face/v1.0";
	private final String FindSimilarsQuery = "findsimilars";
	private final String DetectQuery = "detect";
	private final String FaceListsQuery = "facelists";
	private final String PersistedFacesQuery = "persistedfaces";
	private final String subscriptionKey = "849ef8884bb04ca48e71abb5af9d5541";
	
	@Override
	public String Detect(File imageStream, boolean returnFaceId, boolean returnFaceLandmarks) {
		HttpClient httpclient = HttpClients.createDefault();

        try
        {
        	//Parameters and Headers
            URIBuilder builder = new URIBuilder(DEFAULT_API_ROOT + "/" + DetectQuery);
            builder.setParameter("returnFaceId", String.valueOf(returnFaceId));
            HttpPost request = new HttpPost(builder.build());
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
            // Request body
            request.setEntity(new FileEntity(imageStream, ContentType.APPLICATION_OCTET_STREAM));
            HttpEntity entity = httpclient.execute(request).getEntity();

            if (entity != null) 
            {
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        
		return null;
	}

	@Override
	public String FindSimilar(String faceId, String faceListId, String largeFaceListId,
			int maxNumOfCandidatesReturned) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String AddFaceToFaceList(int faceListId, File imageStream, String userData) {
		HttpClient httpclient = HttpClients.createDefault();

        try
        {
        	//Parameters and Headers
            URIBuilder builder = new URIBuilder(DEFAULT_API_ROOT + "/" + FaceListsQuery + "/" + faceListId + 
            		"/" + PersistedFacesQuery + "?userData="+ userData);
            
//            builder.setParameter("returnFaceId", String.valueOf(returnFaceId));
            HttpPost request = new HttpPost(builder.build());
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body
            request.setEntity(new FileEntity(imageStream, ContentType.APPLICATION_OCTET_STREAM));

            HttpEntity entity = httpclient.execute(request).getEntity();

            if (entity != null) 
            {
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        return null;
	}
	
	
}
