package de.htwberlin.benutzer_manager.api.domain;

public class InvalidUsernameException extends Exception {

	
	private static final long serialVersionUID = 5289522436651327126L;


	public InvalidUsernameException(String message) {
		super("Benutzername ungültig oder bereits vergeben: " + message);
	}
}
