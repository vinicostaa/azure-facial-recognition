package br.usjt.congnitive.spring.service;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.usjt.congnitive.spring.dao.FaceDAO;
import br.usjt.congnitive.spring.model.Face;

public class FaceServiceImpl implements FaceService {

	private final String DEFAULT_API_ROOT = "https://brazilsouth.api.cognitive.microsoft.com/face/v1.0";
	private final String FindSimilarsQuery = "findsimilars";
	private final String personGroupId = "pi-grupo-2-usjt";
	private final String DetectQuery = "detect";
	private final String FaceListsQuery = "facelists";
	private final String PersistedFacesQuery = "persistedfaces";
	private final String subscriptionKey = "849ef8884bb04ca48e71abb5af9d5541";
	
	private FaceDAO faceDAO;
	

	public void setFaceDAO(FaceDAO faceDAO) {
		this.faceDAO = faceDAO;
	}

	@Override
	public String Detect(File imageStream, boolean returnFaceId, boolean returnFaceLandmarks) {
		HttpClient httpclient = HttpClients.createDefault();

		try {
			// Parameters and Headers
			URIBuilder builder = new URIBuilder(DEFAULT_API_ROOT + "/" + DetectQuery);
			builder.setParameter("returnFaceId", String.valueOf(returnFaceId));
			HttpPost request = new HttpPost(builder.build());
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			// Request body
			request.setEntity(new FileEntity(imageStream, ContentType.APPLICATION_OCTET_STREAM));
			HttpEntity entity = httpclient.execute(request).getEntity();

			if (entity != null) {
				String json = EntityUtils.toString(entity);
				Type listType = new TypeToken<List<Face>>() {
				}.getType();
				ArrayList<Face> c = new Gson().fromJson(json, listType);
				return c.get(0).getFaceId();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@Override
	public List<Face> FindSimilar(String faceId, int maxNumOfCandidatesReturned) {
		HttpClient httpclient = HttpClients.createDefault();

		try {
			// Parameters and Headers
			URIBuilder builder = new URIBuilder(DEFAULT_API_ROOT + "/" + FindSimilarsQuery);

			HttpPost request = new HttpPost(builder.build());
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			request.addHeader("content-type", "application/json");

			// Request body
			JSONObject json = new JSONObject();
			json.put("faceId", faceId.toString());
			json.put("faceListId", this.personGroupId);
			json.put("maxNumOfCandidatesReturned", maxNumOfCandidatesReturned);
			json.put("mode", "matchPerson");

			StringEntity params = new StringEntity(json.toString());

			request.setEntity(params);

			HttpEntity entity = httpclient.execute(request).getEntity();

			if (entity != null) {
				String jsonResult = EntityUtils.toString(entity);
				Type listType = new TypeToken<List<Face>>() {
				}.getType();
				ArrayList<Face> faces = new Gson().fromJson(jsonResult, listType);
				return faces;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@Override
	public Face AddFaceToFaceList(File imageStream, String userData) {
		HttpClient httpclient = HttpClients.createDefault();

		try {
			// Parameters and Headers
			URIBuilder builder = new URIBuilder(DEFAULT_API_ROOT + "/" + "persongroups" + "/" + this.personGroupId + "/"+
				"persons"	+ PersistedFacesQuery + "?userData=" + userData.replaceAll("\\s+", ""));

			HttpPost request = new HttpPost(builder.build());
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			// Request body
			request.setEntity(new FileEntity(imageStream, ContentType.APPLICATION_OCTET_STREAM));

			HttpEntity entity = httpclient.execute(request).getEntity();

			if (entity != null) {
				String json = EntityUtils.toString(entity);
				Face c = new Gson().fromJson(json, Face.class);
				return c;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@Override
	@Transactional
	public Face getFaceById(int id) {
		return this.faceDAO.getFaceById(id);
	}

	@Override
	@Transactional
	public Face getFaceByClientId(int id) {
		return this.faceDAO.getFaceByClientId(id);
	}

}
