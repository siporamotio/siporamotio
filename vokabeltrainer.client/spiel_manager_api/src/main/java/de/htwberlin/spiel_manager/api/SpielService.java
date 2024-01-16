package de.htwberlin.spiel_manager.api;

import java.util.List;
import java.util.Map;

import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.spiel_manager.api.domain.NotEnoughPlayerException;
import de.htwberlin.spiel_manager.api.domain.Spiel;
import de.htwberlin.vokabel_manager.api.domain.Datei;
import de.htwberlin.vokabel_manager.api.domain.Quizfrage;

/**
 * Programmschnittstelle der Komponente Spiel_manager.
 *
 */
public interface SpielService {

	/**
	 * dient der Erstellung eines neuen Quizduelles
	 * @param spieler Liste an Nutzern, die am Duell teilnehmen
	 * @param einheit Einheit auf die sich die Spieler geeinigt haben
	 * @return gibt das erstellte Duell zurück
	 * @throws NotEnoughPlayerException Jedes Duell muss aus mindestens zwei Spielern bestehen. 
	 * Werden weniger als zwei Spielern übergeben, wird diese Exception geworfen.
	 */
	Spiel erstelleSpiel(List<Benutzerprofil> spieler, Datei datei) throws NotEnoughPlayerException;
	
	/**
	 * dient der Abfrage der nächsten Vokabelfrage
	 * @param duell Duell, zu dem die nächste Frage angefordert wird
	 * @param nutzer Nutzer, der die nächste Frage beantworten soll
	 * @return gibt die nächste Vokabelfrage zurück. Nachdem alle 10 Fragen des Quizzes zurückgegeben wurden wird null zurückgegeben
	 */
	public Quizfrage naechsteQuizfrage(Spiel duell, Benutzerprofil nutzer);

	/**
	 * speichert eine übergebene Antwort durch Erstellung der Antwortzuordnung.
	 * Dabei wird der Spielstand im Duell getrackt.
	 * 
	 * @param spiel        Duell, zu dem die Antwort abgegeben wurde
	 * @param benutzer       Spieler, der die Antwort abgegeben hat
	 * @param quizfrage Quizfrage, die der Spieler beantwortet hat
	 * @param antwort      Antwort, die der Spieler abgegeben hat
	 * @return gibt true zurück, wenn die Antwort erfolgreich gespeichert wurde;
	 *         ansonsten false
	 */
	public boolean benutzerantwortSpeichern(Spiel spiel, Benutzerprofil nutzer, Quizfrage vokabelfrage, String antwort);

	

	/**
	 * mit Aufruf der Methode wird das Spiel wird beendet. 
	 * @param duell das Duell, welches beendet werden soll
	 * @return gibt den Spielstand des Duells zurück
	 */
	public Map<Benutzerprofil, Integer> spielBeenden(Spiel duell);


}
