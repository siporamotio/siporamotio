package de.htwberlin.benutzer_manager.api.domain;

public class UserAlreadyExistsException extends Exception {


	
	private static final long serialVersionUID = -883722361876930792L;

	public UserAlreadyExistsException(String message, Exception ex) {
		super("Benutzer existiert bereits: " + message);
	}

}
