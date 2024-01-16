package de.htwberlin.benutzer_manager.api.domain;

import jakarta.persistence.NoResultException;

public class UserNotFoundException extends Exception {
	

	
	private static final long serialVersionUID = -5672812881802153891L;

	public UserNotFoundException(String message, NoResultException ex) {
		super("Benutzer nicht gefunden: " + message);
	}

}
