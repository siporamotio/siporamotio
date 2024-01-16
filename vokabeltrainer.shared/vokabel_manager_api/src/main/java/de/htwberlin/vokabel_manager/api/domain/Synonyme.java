package de.htwberlin.vokabel_manager.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Synonyme {

	@Id
	@Column(name = "Synonym_ID")
	private long id;
	private String bedeutung;
	
	

	public Synonyme() {
		
	}

	public Synonyme(String bedeutung) {
		super();
		this.bedeutung = bedeutung;
	}

	public String getBedeutung() {
		return bedeutung;
	}

	public void setBedeutung(String bedeutung) {
		this.bedeutung = bedeutung;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return bedeutung;
	}

}
