package de.htwberlin.vokabel_manager.api.domain;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;


@Entity
public class Quizfrage {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "Quizfrage_ID")
	private long id;
	
	private String frage;
	private String richtigeAntwort;
	@ElementCollection
	@CollectionTable(name="QUIZFRAGE_FALSCHE_ANTWORTEN", joinColumns=@JoinColumn(name="quizfrage_id"))
	private List<String> falscheAntworten;
	
	@Transient
	private List<String> alleAntworten;
	
	
	
	public Quizfrage() {
		
	}

	public Quizfrage(String frage, String richtigeAntwort, List<String> falscheAntworten) {
		super();
		this.frage = frage;
		this.richtigeAntwort = richtigeAntwort;
		this.falscheAntworten = falscheAntworten;
		
		alleAntworten = new ArrayList<String>();
		alleAntworten.add(richtigeAntwort);
		alleAntworten.addAll(falscheAntworten);
		Collections.shuffle(alleAntworten);
	}
	
	public String getFrage() {
		return frage;
	}
	public void setFrage(String frage) {
		this.frage = frage;
	}
	public String getRichtigeAntwort() {
		return richtigeAntwort;
	}
	public void setRichtigeAntwort(String richtigeAntwort) {
		this.richtigeAntwort = richtigeAntwort;
	}
	public List<String> getFalscheAntworten() {
		return falscheAntworten;
	}
	public void setFalscheAntworten(List<String> falscheAntworten) {
		this.falscheAntworten = falscheAntworten;
	}
	public List<String> getAllAntwortenAsList() {
		return alleAntworten;
	}

	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}

//	private String frage;
//	private String vokabelStartwort; // Vokabel für Quizfrage
//	private List<List<String>> richtigeAntwort;
//	private List<List<String>> falscheAntwort1;
//	private List<List<String>> falscheAntwort2;
//	//private Vokabel vokabel;
//
//	public Quizfrage(String frage, List<List<String>> falscheAntwort1, List<List<String>> falscheAntwort2,
//			List<List<String>> richtigeAntwort, String vokabelStartwort) {
//
//		super();
//		Vokabel vokabel = new Vokabel();
//		Vokabel falscheUebersetzung1 = new Vokabel();
//		Vokabel falscheUebersetzung2 = new Vokabel();
//		this.falscheAntwort1 = falscheUebersetzung1.getUebersetzung();
//		this.falscheAntwort2 = falscheUebersetzung2.getUebersetzung();
//		this.richtigeAntwort = vokabel.getUebersetzung();
//		this.vokabelStartwort = vokabel.getStartwort();
//		this.frage = frage;
//
//	}
//
//	public String getFrage() {
//		return frage;
//	}
//
//	public void setFrage(String frage) {
//		this.frage = frage;
//	}
//
//	public String getVokabelStartwort() {
//		return vokabelStartwort;
//	}
//
//	public void setVokabelStartwort(String vokabelStartwort) {
//		this.vokabelStartwort = vokabelStartwort;
//	}
//
//	public List<List<String>> getRichtigeAntwort() {
//		return richtigeAntwort;
//	}
//
//	public void setRichtigeAntwort(List<List<String>> richtigeAntwort) {
//		this.richtigeAntwort = richtigeAntwort;
//	}
//
//	public List<List<String>> getFalscheAntwort1() {
//		return falscheAntwort1;
//	}
//
//	public void setFalscheAntwort1(List<List<String>> falscheAntwort1) {
//		this.falscheAntwort1 = falscheAntwort1;
//	}
//
//	public List<List<String>> getFalscheAntwort2() {
//		return falscheAntwort2;
//	}
//
//	public void setFalscheAntwort2(List<List<String>> falscheAntwort2) {
//		this.falscheAntwort2 = falscheAntwort2;
//	}
//
////	//public Vokabel getVokabel() {
////	//	return vokabel;
////	}
////
////	public void setVokabel(Vokabel vokabel) {
////		this.vokabel = vokabel;
////	}


}
