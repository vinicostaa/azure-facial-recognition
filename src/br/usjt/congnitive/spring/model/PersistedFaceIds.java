package br.usjt.congnitive.spring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "persistedfaceids")
public class PersistedFaceIds {
	
	@Id
	@Column(name="persistedfaceid")
	private String persistedfaceid;
	
	@ManyToOne
	@JoinColumn(name = "face_id", nullable = false)
	@JsonIgnore
	private Face face;
	
	

	public String getPersistedfaceid() {
		return persistedfaceid;
	}

	public void setPersistedfaceid(String persistedfaceid) {
		this.persistedfaceid = persistedfaceid;
	}

	
	public Face getFace() {
		return face;
	}

	public void setFace(Face face) {
		this.face = face;
	}
	
}
