package br.usjt.congnitive.spring.model;

import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "face")
public class Face {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String personId;
	
	private String path;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	@JsonIgnore
	private Client cliente;
	
	@OneToMany(mappedBy="persistedfaceid", fetch = FetchType.LAZY)
	@JsonIgnore
    private Set<PersistedFaceIds> persistedfaceids;

	@Transient
	private ArrayList<String> base64image;
	
	@Transient
	private String faceId;
	
	private String userData;
	
	private String name;
	
	@Transient
	private String persistedFaceId;
	
	@Transient
	private ArrayList<Candidates> candidates;
	
	
	public ArrayList<Candidates> getCantidates() {
		return candidates;
	}

	public void setCantidates(ArrayList<Candidates> cantidates) {
		this.candidates = cantidates;
	}

	public Set<PersistedFaceIds> getPersistedfaceids() {
		return persistedfaceids;
	}

	public void setPersistedfaceids(Set<PersistedFaceIds> persistedfaceids) {
		this.persistedfaceids = persistedfaceids;
	}

	public String getPersistedFaceId() {
		return persistedFaceId;
	}

	public void setPersistedFaceId(String persistedFaceId) {
		this.persistedFaceId = persistedFaceId;
	}


	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFaceId() {
		return faceId;
	}

	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}

	public Client getCliente() {
		return cliente;
	}

	public void setCliente(Client cliente) {
		this.cliente = cliente;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<String> getBase64image() {
		return base64image;
	}

	public void setBase64image(ArrayList<String> base64image) {
		this.base64image = base64image;
	}
}
