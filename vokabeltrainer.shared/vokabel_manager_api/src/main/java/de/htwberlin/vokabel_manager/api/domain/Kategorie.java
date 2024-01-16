package de.htwberlin.vokabel_manager.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Kategorie {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "Kategorie_ID")
	private long id;
	private String name;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Kategorie(String name) {
		this.name = name;
	}
	
	public Kategorie() {
		
	}
	
	@Override
	public String toString(){
		return name;
	}


}
