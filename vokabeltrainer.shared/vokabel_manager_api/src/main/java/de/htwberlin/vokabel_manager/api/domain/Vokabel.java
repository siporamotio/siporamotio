package de.htwberlin.vokabel_manager.api.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Vokabel {
	
	@Id
	@Column(name = "Vokabel_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ElementCollection
	@CollectionTable(name="VOKABEL_BEDEUTUNG_FREMDSPRACHE", joinColumns=@JoinColumn(name="vokabel_id"))
	@Column(name="bedeutung")
	private List<String> bedeutungen_fremdsprache;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Bedeutungen> bedeutungen_uebersetzungssprache;

	 @ManyToOne
	 @JoinColumn(name="Datei_ID", nullable=false)
	private Datei datei;
	
	public Vokabel(List<String> bedeutungen_fremdsprache, List<Bedeutungen> bedeutungen_uebersetzungssprache) {
		super();
		this.bedeutungen_fremdsprache = bedeutungen_fremdsprache;
		this.bedeutungen_uebersetzungssprache = bedeutungen_uebersetzungssprache;
	}
	
	public Vokabel() {
		
	}
	
	public List<String> getBedeutungen_fremdsprache() {
		return bedeutungen_fremdsprache;
	}
	
	public void setBedeutungen_fremdsprache(List<String> bedeutungen_fremdsprache) {
		this.bedeutungen_fremdsprache = bedeutungen_fremdsprache;
	}
	
	public List<Bedeutungen> getBedeutungen_uebersetzungssprache() {
		return bedeutungen_uebersetzungssprache;
	}
	
	public void setBedeutungen_uebersetzungssprache(List<Bedeutungen> bedeutungen_uebersetzungssprache) {
		this.bedeutungen_uebersetzungssprache = bedeutungen_uebersetzungssprache;
	}

	public Datei getDatei() {
		return datei;
	}

	public void setDatei(Datei datei) {
		this.datei = datei;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id=id;
	}

	@Override
	public String toString() {
		return "Vokabel [bedeutungen_fremdsprache=" + bedeutungen_fremdsprache + ", bedeutungen_uebersetzungssprache="
				+ bedeutungen_uebersetzungssprache + ", datei=" + datei + "]";
	}
	
	
//
//	private String startwort;
//	private List<List<String>>  uebersetzung;
//	
//
//	public Vokabel() {
//		// TODO Auto-generated constructor stub
//	}
//
//	public Vokabel(String startwort, List<List<String>> string) {
//		super();
//
//		this.startwort = startwort;
//		this.uebersetzung = string;
//	}
//
//
////	public Kategorie getKategorie() {
////		return kategorie;
////	}
////
////	public void setKategorie(Kategorie kategorie) {
////		this.kategorie = kategorie;
////	}
//
//
//	public String getStartwort() {
//		return startwort;
//	}
//
//	public void setStartwort(String startwort) {
//		this.startwort = startwort;
//	}
//
//	public List<List<String>> getUebersetzung() {
//		return uebersetzung;
//	}
//
//	public void setUebersetzung(List<List<String>> uebersetzung) {
//		this.uebersetzung = uebersetzung;
//	}

}
