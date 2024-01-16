package de.htwberlin.vokabel_manager.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InvalidNameException;

import de.htwberlin.vokabel_manager.api.domain.Datei;
import de.htwberlin.vokabel_manager.api.domain.Kategorie;
import de.htwberlin.vokabel_manager.api.domain.Quizfrage;

/**
 * Programmschnittstelle der Komponente Vokabel-Manager.
 *
 */
public interface VokabelService {
	
	/**
	 * lädt die Einheit mit dem angegebenen Namen
	 * @param name Name der Einheit
	 * @return die geladene Einheit und ihre Vokabeln
	 * @throws InvalidNameException falls Name ungültig
	 */
	public Datei ladeDatei(String name) throws InvalidNameException;
	
	/**
	 * lädt alle Einheiten zur übergebenen Kategorie
	 * @param k die Kategorie
	 * @return Liste aller Einheiten in der Kategorie
	 */
	public List<Datei> ladeDateiByKategorie(Kategorie k);
	
	/**
	 * löscht die übergebene Einheit aus der Datenbank und vom Dateisystem
	 * @param einheit die Einheit, welche gelöscht werden soll
	 * @return ob das Löschen erfolgreich war
	 * @throws IOException falls ein Fehler beim Löschen der .CSV Datei auftritt
	 * @throws SQLException falls ein Fehler beim Löschen in der Datenbank auftritt
	 */
	public boolean loescheDatei(Datei datei) throws IOException, SQLException;
	
	/**
	 * lädt alle Kategorien und die dazu zugehörigen Einheiten und Vokablen
	 * @return eine Liste der verfügbaren Kategorien
	 */
	public List<Kategorie> ladeKategorien();
	
	/**
	 * lädt die Kategorie mit dem angegebenen Namen
	 * @param name Name der Kategorie
	 * @return die geladene Kategorie
	 * @throws InvalidNameException falls Name ungültig
	 */
	public Kategorie ladeKategorie(String name) throws InvalidNameException;
	

	/**
	 * generiert zufällige Vokabelfragen, die Einheit bildet die Grundlage zum erzeugen der Fragen.
	 * @param einheit die Einheit aus der die Fragen generiert werden
	 * @param anzahl die Länge der zurückgegebenen Liste
	 * @return eine List der länge @anzahl mit den generierten Fragen
	 * @throws IllegalStateException falls anzahl 0 oder negativ
	 */
	public List<Quizfrage> generiereFragen(Datei einheit, int anzahl) throws IllegalStateException;
	
	
	/**
	 * improtiert die CSV-Dateien zum lernen der Vokabeln aus dem Classpath in die Datenbank. 
	 * @return ob der Import erfolgreich war
	 * @throws IOException falls ein Fehler beim laden der .CSV Dateien auftritt
	 * @throws SQLException falls ein Fehler bei der Datenbank auftritt
	 */
	public boolean improtiereCSVDateien() throws IOException,SQLException;
	
	/**
	 * improtiert die übergebene CSV-Datei zum lernen der Vokabeln in die Datenbank. 
	 * @param datei die Datei, welche importiert werden soll
	 * @return ob der Import erfolgreich war
	 * @throws IOException falls ein Fehler beim laden der .CSV Datei auftritt
	 * @throws SQLException falls ein Fehler bei der Datenbank auftritt
	 */
	public boolean improtiereCSVDatei(String datei) throws IOException,SQLException;
	
	/**
	 * legt eine leere Datenbank an
	 * @throws SQLException falls ein Fehelr beim anlegen der Datenbank auftritt
	 */
	public void executeSQLFile(String file, String jdbcUrl, String username, String password) throws SQLException, IOException;
	


    
}
