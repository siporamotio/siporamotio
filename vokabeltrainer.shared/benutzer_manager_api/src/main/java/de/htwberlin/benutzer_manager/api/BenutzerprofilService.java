package de.htwberlin.benutzer_manager.api;

import java.util.List;

import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.benutzer_manager.api.domain.InvalidUsernameException;
import de.htwberlin.benutzer_manager.api.domain.UserAlreadyExistsException;
import de.htwberlin.benutzer_manager.api.domain.UserNotFoundException;

/**
 * Programmschnittstelle der Komponente Benutzer_Manager.
 *
 */

public interface BenutzerprofilService {
	/**
	 * Erstellt ein neues Benutzerprofil mit den angegebenen Benutzerdaten.
	 * 
	 * @param name     Der Name des Benutzers.
	 * @param passwort Das Passwort des Benutzers.
	 * @param email    Die E-Mail-Adresse des Benutzers.
	 * @return Das erstellte Benutzerprofil.
	 * 
	 * 
	 * @throws InvalidUsernameException   Die Methode wirft eine Exception, wenn die
	 *                                    übergebenen Nutzername ungültig ist.
	 *                                    Ungültige Nutzernamen sind leere Strings
	 *                                    (nur Leerzeichen zählen auch als leer).
	 *                                    Außerdem darf Nutzername keine
	 *                                    Sonderzeichen enthalten und muss einmalig
	 *                                    sein.
	 * @throws UserAlreadyExistsException wenn die übergebenen Name, schon von einem
	 *                                    anderen Benutzer verwendet werden.
	 */
	Benutzerprofil erstelleBenutzerprofil(String benutzername/* , String passwort, String email */)
			throws InvalidUsernameException, UserAlreadyExistsException;

	/**
	 * Gibt das Benutzerprofil eines Benutzers anhand der Benutzerid zurück.
	 * 
	 * @param name Der Name des Benutzers.
	 * @return Das Benutzerprofil des Benutzers.
	 * @throws UserNotFoundException
	 */
	Benutzerprofil findeBenutzerprofil(String name) throws UserNotFoundException;

	/**
	 * Löscht das Benutzerprofil eines Benutzers.
	 * 
	 * @param benutzer Das zu löschende Benutzerprofil.
	 * @return
	 * @throws UserNotFoundException
	 */
	boolean loescheBenutzerprofil(Benutzerprofil benutzer) throws UserNotFoundException;

	/**
	 * 
	 * gibt alle Benutzer zurück
	 * 
	 * @return eine Liste aller Benutzer
	 * @throws UserNotFoundException
	 */
	public List<Benutzerprofil> findeAllBenutzer() throws UserNotFoundException;

}
