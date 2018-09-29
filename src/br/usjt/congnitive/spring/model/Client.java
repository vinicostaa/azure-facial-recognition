package br.usjt.congnitive.spring.model;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/***
 * 
 * @author maykellvinicius
 * Class Client bean
 */
@Entity
@Table(name="cliente")
@Proxy(lazy = false)
public class Client {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String nome;
	
	private String cpf;
	
	private String rg;
	
	private String telefone;
	
	private String email;
	
//	@OneToMany(mappedBy = "cliente", targetEntity = Face.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	private List<Face>  face;
	
	@OneToMany(mappedBy="cliente", fetch = FetchType.LAZY)
	@JsonIgnore
    private Set<Face> faces;
	
	public Set<Face> getFace() {
		return faces;
	}

	public void setFace(Set<Face> faces) {
		this.faces = faces;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	@Override
	public String toString(){
		return "id= " + id + ", name= "+ nome +", cpf= " + cpf + ", rg= "+ rg + ", telefone= " + telefone + ", email= " + email;
				
	}

}
