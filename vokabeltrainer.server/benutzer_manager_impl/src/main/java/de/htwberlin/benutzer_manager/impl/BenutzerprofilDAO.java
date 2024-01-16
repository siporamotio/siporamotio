package de.htwberlin.benutzer_manager.impl;

import java.util.List;

import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.benutzer_manager.api.domain.InvalidUsernameException;
import de.htwberlin.benutzer_manager.api.domain.UserAlreadyExistsException;
import de.htwberlin.benutzer_manager.api.domain.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class BenutzerprofilDAO {

	private EntityManager entityManager;
	private static BenutzerprofilDAO instance = null;

	
	public EntityTransaction getEntityTransaction() {
		  if (entityManager != null) {
	            return entityManager.getTransaction();
	        }
	        return null; // oder eine andere geeignete Reaktion auf null EntityManager
	    }
	

	/**
	 * Konstruktor für BenutzerprofilDAO. Erstellen eines EntityMangers.
	 */
	public BenutzerprofilDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("VokabelDatenbank");
		entityManager = emf.createEntityManager();
		
	}

	/**
	 * Methode zur Rückgabe einer BenutzerprofilDAO Instanz.
	 * 
	 */
	public static BenutzerprofilDAO getBenutzerprofilDAO() {
		 if (instance == null) {
	            instance = new BenutzerprofilDAO();
	        }
	        return instance;
	}

	/**
	 * 
	 * @param benutzer
	 * @return
	 */
	public Benutzerprofil speichereBenutzer(Benutzerprofil benutzer) throws InvalidUsernameException, UserAlreadyExistsException {
		
		
		EntityTransaction transaction = getEntityTransaction();
        try {
            transaction.begin();
          
            entityManager.persist(benutzer);
            transaction.commit();
            return benutzer;
        } catch (Exception ex) {
            transaction.rollback();
            throw new UserAlreadyExistsException("Benutzername ungültig oder bereits vergeben: " + benutzer.getBenutzername(),ex);
        }
	}

	/**
	 * Sucht einen Benutzer anhand seiner ID in der Datenbank.
	 * 
	 * @param id die ID des gesuchten Nutzers
	 * @return der Benuutzer oder null, falls der Nutzer nicht existiert
	 */
	public Benutzerprofil findeBenutzerById(Long id) {
		return entityManager.find(Benutzerprofil.class, id);
	}

	/**
	 * Sucht einen Nutzer anhand seines Benutzername in der Datenbank.
	 * 
	 * @param Benutzername der Benutzername des gesuchten Benutzers
	 * @return der Nutzer oder null, falls der Nutzer nicht existiert
	 */

	public Benutzerprofil findeBenutzerByName(String benutzername) throws UserNotFoundException {
		
		 TypedQuery<Benutzerprofil> query = entityManager.createQuery("SELECT n FROM Benutzerprofil n WHERE n.benutzername = :benutzername", Benutzerprofil.class);
	        query.setParameter("benutzername", benutzername);
	        try {
	            return query.getSingleResult();
	        } catch (NoResultException ex) {
	            throw new UserNotFoundException("Benutzer nicht gefunden", ex);
	        }

	}

	/**
	 * Löscht einen bestimmten Benutzer aus der Datenbank.
	 * 
	 * @param benutzer: der zu löschende Benutzer
	 */
	public void loescheBenutzer(Benutzerprofil benutzer) {
		EntityTransaction transaction = getEntityTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.contains(benutzer) ? benutzer : entityManager.merge(benutzer));
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            // Behandeln Sie die Ausnahme angemessen
        }
	}

	/**
	 * Gibt alle Beutzer aus der Datenbank als Liste zurück.
	 * 
	 * @return eine Liste mit allen Benutzern in der Datenbank
	 */
	public List<Benutzerprofil> findeAlleBenutzer() {
		return entityManager.createQuery("SELECT n FROM Benutzerprofil n", Benutzerprofil.class).getResultList();
	}
}
