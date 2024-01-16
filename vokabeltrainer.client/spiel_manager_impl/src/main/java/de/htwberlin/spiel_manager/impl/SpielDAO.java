package de.htwberlin.spiel_manager.impl;

import java.util.List;

import de.htwberlin.benutzer_manager.api.domain.Benutzerprofil;
import de.htwberlin.spiel_manager.api.domain.Benutzerantwort;
import de.htwberlin.spiel_manager.api.domain.Spiel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

public class SpielDAO {
	 private EntityManager em;
	 private static SpielDAO instance = null;

	private SpielDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("VokabelDatenbank");
	    em = emf.createEntityManager();
	}
	
	public EntityTransaction getEntityTransaction() {
		  if (em != null) {
	            return em.getTransaction();
	        }
	        return null; // oder eine andere geeignete Reaktion auf null EntityManager
	    }

	public static SpielDAO getSpielDAO() {
		if (instance == null) {
			instance = new SpielDAO();
		}
		return instance;
	}

	public void saveBenutzerantwort(Benutzerantwort benutzerantwort) {
		EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(benutzerantwort);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Hier könntest du das Logging-System verwenden
        }
        	
	}
	
	@SuppressWarnings("unchecked")
	public List<Benutzerantwort> getBenutzerantwort(Spiel spiel, Benutzerprofil spieler) {
		EntityTransaction transaction = em.getTransaction();
        List<Benutzerantwort> benutzerantworten = null;
        try {
            transaction.begin();

            String queryString = "FROM Benutzerantwort AS ba WHERE ba.spiel = :spiel AND ba.spieler = :spieler";
            Query query = em.createQuery(queryString)
                           .setParameter("spiel", spiel)
                           .setParameter("spieler", spieler);
            benutzerantworten = query.getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Hier könntest du das Logging-System verwenden
        }
        return benutzerantworten;

	}
	
	public void speichereSpiel(Spiel spiel) {
		
		 EntityTransaction transaction = em.getTransaction();
	        try {
	            transaction.begin();
	            em.persist(spiel);
	            transaction.commit();
	        } catch (Exception e) {
	            if (transaction.isActive()) {
	                transaction.rollback();
	            }
	            e.printStackTrace(); 
	        }

	}
	
}
