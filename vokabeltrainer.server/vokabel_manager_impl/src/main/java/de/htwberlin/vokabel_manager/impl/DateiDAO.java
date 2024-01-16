package de.htwberlin.vokabel_manager.impl;

import java.util.List;

import javax.naming.InvalidNameException;

import de.htwberlin.vokabel_manager.api.domain.Datei;
import de.htwberlin.vokabel_manager.api.domain.Kategorie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class DateiDAO {
	private EntityManager em;
    private static DateiDAO e = null;

    private DateiDAO() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("VokabelDatenbank");
        em = emf.createEntityManager(); // Initialisierung des EntityManager-Objekts hinzugefügt
    }
	public static DateiDAO getDateiDao() {
		if (e == null) {
			e = new DateiDAO();
		}
		return e;
	}

	public void saveEinheit(Datei einheit) {
		em.persist(einheit);
	}

	public void deleteEinheit(Datei einheit) {
		em.remove(einheit);
	}

	public Datei getDateiByName(String name) throws InvalidNameException {
		
		    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		    CriteriaQuery<Datei> criteriaQuery = criteriaBuilder.createQuery(Datei.class);
		    Root<Datei> root = criteriaQuery.from(Datei.class);

		   
		    Predicate predicate = criteriaBuilder.equal(root.get("bezeichnung"), name);
		    criteriaQuery.where(predicate);

		 
		    List<Datei> resultList = em.createQuery(criteriaQuery).getResultList();

		    if (resultList.isEmpty()) {
		        // Keine Ergebnisse gefunden
		        return null;
		    }

		    if (resultList.size() > 1) {
		        
		        throw new NonUniqueResultException("Mehr als ein Ergebnis gefunden für " + name);
		    }

		    // Eindeutiges Ergebnis zurückgeben
		    return resultList.get(0);
		}

	

	public Datei getById(long id) {
		return em.find(Datei.class, id);
	}

	public List<Datei> getByKategorie(Kategorie k) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Datei> criteriaQuery = criteriaBuilder.createQuery(Datei.class);
		Root<Datei> root = criteriaQuery.from(Datei.class);

	
		Predicate predicate = criteriaBuilder.equal(root.get("kategorie"), k);
		criteriaQuery.where(predicate);

		return em.createQuery(criteriaQuery).getResultList();
	}

	public EntityTransaction getEntityTransaction() {
		 if (em != null) {
	            return em.getTransaction();
	        }
	        return null; 
	}
}
