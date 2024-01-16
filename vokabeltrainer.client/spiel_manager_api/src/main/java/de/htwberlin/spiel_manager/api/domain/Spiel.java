package de.htwberlin.spiel_manager.api.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.vokabel_manager.api.domain.Datei;
import de.htwberlin.vokabel_manager.api.domain.Quizfrage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Spiel {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "Spiel_ID")
	private long id;
	
	@ManyToMany
	private List<Benutzerprofil> spieler;
	
	@ManyToOne(cascade = {CascadeType.REMOVE}, targetEntity = de.htwberlin.vokabel_manager.api.domain.Datei.class)
	@JoinColumn(name="datei_ID", referencedColumnName = "datei_ID")
	private Datei datei;   //Für jedes Spiel sollte man wissen aus welcherDatei die Fragen stammten
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Quizfrage>  quizfragen; // Die Quizfragen für das Spiel
	
	@ElementCollection
	@CollectionTable(name = "spielstaende", joinColumns = @JoinColumn(name = "Spiel_id"))
	@Column(name = "punktzahl")
	@MapKeyJoinColumn(name = "Benutzer_id")
	private Map<Benutzerprofil, Integer> spielstand;
	

	 public Spiel(List<Benutzerprofil> spieler, Datei datei, List<Quizfrage> quizfragen) {
	        this.spieler = spieler;
	        this.datei = datei;
	        this.quizfragen = quizfragen;
	        initializeSpielstaende();
	    }

	    private void initializeSpielstaende() {
	        spielstand = new HashMap<>();
	        spieler.forEach(n -> spielstand.put(n, 0));
	    }

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public List<Benutzerprofil> getSpieler() {
		return spieler;
	}


	public void setSpieler(List<Benutzerprofil> spieler) {
		this.spieler = spieler;
	}


	public Datei getDatei() {
		return datei;
	}


	public void setDatei(Datei datei) {
		this.datei = datei;
	}


	public List<Quizfrage> getQuizfragen() {
		return quizfragen;
	}


	public void setQuizfragen(List<Quizfrage> quizfragen) {
		this.quizfragen = quizfragen;
	}


	public Map<Benutzerprofil, Integer> getSpielstand() {
		return spielstand;
	}

	public void setSpielstand(Map<Benutzerprofil, Integer> spielstand) {
		this.spielstand = spielstand;
	}

	


}
