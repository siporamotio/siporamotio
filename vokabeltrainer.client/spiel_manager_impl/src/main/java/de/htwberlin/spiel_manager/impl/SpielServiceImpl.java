package de.htwberlin.spiel_manager.impl;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.spiel_manager.api.SpielService;
import de.htwberlin.spiel_manager.api.domain.Benutzerantwort;
import de.htwberlin.spiel_manager.api.domain.NotEnoughPlayerException;
import de.htwberlin.spiel_manager.api.domain.Spiel;
import de.htwberlin.vokabel_manager.api.VokabelService;
import de.htwberlin.vokabel_manager.api.domain.Datei;
import de.htwberlin.vokabel_manager.api.domain.Quizfrage;
import jakarta.transaction.Transactional;


@Service
public class SpielServiceImpl implements SpielService {

	@Autowired
	private VokabelService vokabelService;
	
	@Transactional
    public Spiel erstelleSpiel(List<Benutzerprofil> spieler, Datei datei) throws NotEnoughPlayerException {
        Objects.requireNonNull(spieler, "Spielerliste darf nicht null sein");
        Objects.requireNonNull(datei, "Datei darf nicht null sein");

        if (spieler.size() < 2) {
            throw new NotEnoughPlayerException();
        } else {
            List<Quizfrage> fragen = vokabelService.generiereFragen(datei, 5);
            Spiel spiel = new Spiel(spieler, datei, fragen);
            SpielDAO.getSpielDAO().speichereSpiel(spiel);
            return spiel;
        }
    }

    @Override
    @Transactional
    public Quizfrage naechsteQuizfrage(Spiel spiel, Benutzerprofil nutzer) {
        Objects.requireNonNull(spiel, "Spiel darf nicht null sein");
        Objects.requireNonNull(nutzer, "Nutzer darf nicht null sein");

        List<Benutzerantwort> antwortzuordnungen = SpielDAO.getSpielDAO().getBenutzerantwort(spiel, nutzer);

        if (antwortzuordnungen.size() < spiel.getQuizfragen().size()) {
            return spiel.getQuizfragen().get(antwortzuordnungen.size());
        } else {
            return null;
        }
    }

		
		@Override
	    @Transactional
	    public boolean benutzerantwortSpeichern(Spiel spiel, Benutzerprofil nutzer, Quizfrage quizfrage, String antwort) {
	        Objects.requireNonNull(spiel, "Spiel darf nicht null sein");
	        Objects.requireNonNull(nutzer, "Nutzer darf nicht null sein");
	        Objects.requireNonNull(quizfrage, "Quizfrage darf nicht null sein");
	        Objects.requireNonNull(antwort, "Antwort darf nicht null sein");

	        Benutzerantwort antwortzuordnung = new Benutzerantwort(antwort, nutzer, quizfrage, spiel);
	        SpielDAO.getSpielDAO().saveBenutzerantwort(antwortzuordnung);

	        // Spielstand aktualisieren
	        if (antwort.equals(quizfrage.getRichtigeAntwort())) {
	            Map<Benutzerprofil, Integer> spielstand = spiel.getSpielstand();
	            spielstand.put(nutzer, spielstand.get(nutzer) + 1);
	            spiel.setSpielstand(spielstand);
	            SpielDAO.getSpielDAO().speichereSpiel(spiel);
	        }

	        return true;
	    }


	@Override
	public Map<Benutzerprofil, Integer> spielBeenden(Spiel spiel) {
		
		Map<Benutzerprofil, Integer> spielstand = spiel.getSpielstand();
        Collections.max(spielstand.entrySet(), Map.Entry.comparingByValue()).getKey();
        
        SpielDAO.getSpielDAO().speichereSpiel(spiel);
        return spiel.getSpielstand();

	}


	

	}

	


