package de.htwberlin.benutzer_manager.api.domain;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "Benutzer")
public class Benutzerprofil{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Benutzer_ID")
	private long id;
	private String benutzername;
	
	
	public Benutzerprofil( String name){
		super();
		this.benutzername = name;
		//this.email = email;
		//this.passwort = passwort;
	}

	public Benutzerprofil( ) {
		
	}

	

	

	public long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getBenutzername() {
		return benutzername;
	}


	public void setBenutzername(String name) {
		this.benutzername = name;
	}



}
