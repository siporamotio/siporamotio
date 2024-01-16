package de.htwberlin.vokabel_manager.api.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Datei {
	
	@Id
	@Column(name = "datei_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String bezeichnung;
	
    private String anfangsSprache;
	private String zielSprache;
	@ManyToOne(cascade = {CascadeType.REMOVE}, targetEntity = de.htwberlin.vokabel_manager.api.domain.Kategorie.class)
	@JoinColumn(name="Kategorie_ID", referencedColumnName = "Kategorie_ID")
	private Kategorie kategorie;
	
	@OneToMany(cascade = {CascadeType.REMOVE}, targetEntity = de.htwberlin.vokabel_manager.api.domain.Vokabel.class, mappedBy="datei")
	private List<Vokabel> vokabeln;
	
	public Datei(String name, String anfangsSprache, String zielSprache, String kategorieName,
			List<Vokabel> vokabeln) {
		super();
		this.bezeichnung = name;
		this.anfangsSprache = anfangsSprache;
		this.zielSprache = zielSprache;
		this.kategorie = new Kategorie(kategorieName);
		this.vokabeln = vokabeln;
	}
	
	public Datei() {
		
	}
	
	

	public String getName() {
		return bezeichnung;
	}

	public void setName(String name) {
		this.bezeichnung = name;
	}

	public String getAnfangsSprache() {
		return anfangsSprache;
	}

	public void setAnfangsSprache(String anfangsSprache) {
		this.anfangsSprache = anfangsSprache;
	}

	public String getZielSprache() {
		return zielSprache;
	}

	public void setZielSprache(String zielSprache) {
		this.zielSprache = zielSprache;
	}

	public Kategorie getKategorie() {
		return kategorie;
	}

	public void setKategorie(Kategorie kategorie) {
		this.kategorie = kategorie;
	}

	public List<Vokabel> getVokablen() {
		if(vokabeln == null) {
			vokabeln = new ArrayList<Vokabel>();
		}
		return vokabeln;
	}

	public void setVokablen(List<Vokabel> vokablen) {
		this.vokabeln= vokablen;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id=id;
	}
	


//	private String anfangssprache;
//	private String zielsprache;
//	private String name;
//	private Kategorie kategorie;
//	private List<Vokabel> vokabeln;
//
//	public Datei(String name, Kategorie kategorie, List<Vokabel> vokabeln) {
//		super();
//		this.name = name;
//		this.kategorie = kategorie;
//		this.vokabeln = vokabeln;
//	}
//
//	public String getAnfangssprache() {
//		return anfangssprache;
//	}
//
//	public void setAnfangssprache(String anfangssprache) {
//		this.anfangssprache = anfangssprache;
//	}
//
//	public String getZielsprache() {
//		return zielsprache;
//	}
//
//	public void setZielsprache(String zielsprache) {
//		this.zielsprache = zielsprache;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public Kategorie getKategorie() {
//		return kategorie;
//	}
//
//	public void setKategorie(Kategorie kategorie) {
//		this.kategorie = kategorie;
//	}
//
//	public List<Vokabel> getVokabeln() {
//		return vokabeln;
//	}
//
//	public void setVokabeln(List<Vokabel> vokabeln) {
//		this.vokabeln = vokabeln;
//	}
//

}
