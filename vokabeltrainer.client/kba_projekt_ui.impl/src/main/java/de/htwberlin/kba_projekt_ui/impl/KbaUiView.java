package de.htwberlin.kba_projekt_ui.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.vokabel_manager.api.domain.Datei;
import de.htwberlin.vokabel_manager.api.domain.Kategorie;
import de.htwberlin.vokabel_manager.api.domain.Quizfrage;



@Component
public class KbaUiView {
	
	
	public void Willkommen() {
		System.out.println("Willkommen zur Vokabeltrainer!");
	}

	public void Hauptmenue() {
		System.out.println("Hauptmenü:");
		System.out.println("Was wollen Sie machen? \nUm fortzufahren, geben Sie bitte immer die entsprechende Nummer für Ihre Auswahl an."
				+ " Ihre Optionen sind numeriert, zum Beispiel:\r\n"
				+ "\r\n"
				+ "1- Aktion 1\r\n"
				+ "2- Aktion 2\r\n"
				+ "3- Aktion 3");
		System.out.println("\r\n1- neues Spiel beginnen \n2- App Beenden");
	}

	public void NutzerAuswahlmenue(List<Benutzerprofil> nutzer) {
		System.out.println("Spieler auswählen:");
		if (nutzer.isEmpty()) {
			System.out.println("Es sind keine Nutzer angemeldet");
		} else {
			System.out.println("Die folgenden Nutzer sind angemeldet :");
	    nutzer.forEach(n -> System.out.println(n.getBenutzername()));
			
		}
		System.out.println("1- neuer Nutzer anlegen \n2- vorhandene Nutzer laden \n3- Spiel starten \n4- zurück");
	}

	public void NutzerAnlegen() {
		System.out.println("Bitte geben Sie den neuen Nutzernamen an: ");
	}

	public void NutzerAngelegt(String username) {
		System.out.println("Nutzer " + username + " wurde erfolgreich angelegt!");
	}

	public void nichtgenugNutzer() {
		System.err.println("Um ein Spiel zu starten müssen mind. zwei Nutzer angemeldet sein!");
	}
	
	public void SpielernameEingeben() {
		System.out.println("Geben Sie den Nutzernamen des zu ladenden Spielers ein:");
	}

	public void ungueltigNutzer() {
		System.out.println("Dies ist kein valider Nutzername!");
	}

	public void Nutzernichtfinden() {
		System.out.println("Es wurde kein Nutzer mit diesem Nutzernamen gefunden");
		
	}

	public void falscheEingabe() {
		System.out.println("Ungültige Eingabe.");
	}

	public void AuswahlKategorie(List<Kategorie> kategorien) {
		System.out.println("Wählen Sie bitte eine Kategorie aus");
		System.out.println("0 für zurück");
		int i = 1;
		for (Kategorie k : kategorien) {
			System.out.println(i + "- " + k.getName());
		i++;
		}
	}

	public void AuswahlDatei(List<Datei> Dateien) {
		System.out.println("Welche Datei möchten Sie spielen?");
		System.out.println("0- für zurück");
		int i = 1;
		for (Datei d : Dateien) {
			System.out.println(i + "- " + d.getName());
			i++;
		}
	}
	
	public void Frage(Quizfrage f) {
		System.out.println("Was ist die richtige Übersetzung für "+f.getFrage());
		int i = 1;
		for(String a : f.getAllAntwortenAsList()) {
			System.out.println(i+"- "+a);
			i++;
		}

	}

	public void AktuellerSpieler(Benutzerprofil spieler) {
		System.out.println("Spieler "+spieler.getBenutzername()+" ist dran.");
		
	}

	public void Lade() {
		System.out.println("Loading...");
		
	}

	public void SpielerFertig(Benutzerprofil spieler) {
		System.out.println("Spieler "+spieler.getBenutzername()+" hat alle Fragen beantwortet.");
	}

	
	public void SpielEnde(Map<Benutzerprofil, Integer> spielstand) {
		System.out.println("Alle Fragen wurden beantwortet. Das Spiel wurde beendet.");
		spielstand.forEach((spieler, punkte) -> {
			System.out.println("Ergebnis: Spieler "+spieler.getBenutzername()+" hat "+punkte+" Punkte erreicht.");
		});
		System.out.println("Drücken Sie 0 um zurück zum Hauptmenü zu gelangen.");
	}
	}


