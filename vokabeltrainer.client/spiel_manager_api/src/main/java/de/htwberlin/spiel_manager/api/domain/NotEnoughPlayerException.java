package de.htwberlin.spiel_manager.api.domain;

public class NotEnoughPlayerException extends Exception{
	
	

	private static final long serialVersionUID = 8644035164050384426L;

	public NotEnoughPlayerException() {
		super("Duell muss aus mindestens zwei Spielern bestehen");
	}

}
