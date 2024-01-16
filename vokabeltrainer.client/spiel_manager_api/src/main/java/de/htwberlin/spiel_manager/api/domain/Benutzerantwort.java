package de.htwberlin.spiel_manager.api.domain;


import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.vokabel_manager.api.domain.Quizfrage;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Benutzerantwort {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String antwort;
	@ManyToOne
	private Benutzerprofil spieler; // Der Benutzer, der die Antwort gegeben hat
	@ManyToOne
	private Quizfrage quizfrage; // Die Quizfrage, auf die sich die Antwort bezieht
	@ManyToOne
	private Spiel spiel;

	

	public Benutzerantwort() {
		
	}

	public Benutzerantwort(String antwort, Benutzerprofil benutzer, Quizfrage quizfrage, Spiel duell) {
		super();
		this.antwort = antwort;
		this.spieler = benutzer;
		this.quizfrage = quizfrage;
		this.spiel = duell;
	}

	public String getAntwort() {
		return antwort;
	}

	public void setAntwort(String antwort) {
		this.antwort = antwort;
	}

	public Benutzerprofil getBenutzer() {
		return spieler;
	}

	public void setBenutzer(Benutzerprofil benutzer) {
		this.spieler = benutzer;
	}

	public Quizfrage getQuizfrage() {
		return quizfrage;
	}

	public void setQuizfrage(Quizfrage quizfrage) {
		this.quizfrage = quizfrage;
	}

	public Spiel getDuell() {
		return spiel;
	}

	public void setDuell(Spiel duell) {
		this.spiel = duell;
	}
}