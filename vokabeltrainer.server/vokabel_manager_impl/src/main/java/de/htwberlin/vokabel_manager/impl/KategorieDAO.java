package de.htwberlin.vokabel_manager.impl;

import java.util.List;

import javax.naming.InvalidNameException;

import de.htwberlin.vokabel_manager.api.domain.Kategorie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

public class KategorieDAO {

	private static KategorieDAO k = null;
	private EntityManager em;
	

	private KategorieDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("VokabelDatenbank");
		em = emf.createEntityManager();
		em.unwrap(org.hibernate.Session.class);
	}

	public static KategorieDAO getKategorieDao() {
		if (k == null) {
			k = new KategorieDAO();
		}
		return k;
	}

	public void saveKategorie(Kategorie kategorie) {
		em.persist(kategorie);

	}

	public Kategorie getKategorieByName(String name) throws InvalidNameException {
		 try {
		        return em.createQuery("SELECT k FROM Kategorie k WHERE k.name = :name", Kategorie.class)
		                .setParameter("name", name)
		                .getSingleResult();
		    } catch (NoResultException e) {
		        // Keine Entität gefunden
		        return null;
		    }
		}
	

	public List<Kategorie> getAll() {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Kategorie> criteriaQuery = criteriaBuilder.createQuery(Kategorie.class);
		criteriaQuery.from(Kategorie.class);

		
		return em.createQuery(criteriaQuery).getResultList();
	}

	public EntityTransaction getEntityTransaction() {
		return em.getTransaction();
	}
}
