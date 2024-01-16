
package de.htwberlin.vokabel_manager.impl;

import de.htwberlin.vokabel_manager.api.domain.Vokabel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class VokabelDAO {

	private static VokabelDAO instance = null;
	private EntityManager entityManager;

	private VokabelDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("VokabelDatenbank");
		entityManager = emf.createEntityManager();
		entityManager.unwrap(org.hibernate.Session.class);
	}

	public static VokabelDAO getVokabelDao() {
		if (instance == null) {
			instance = new VokabelDAO();
		}
		return instance;
	}

	public void saveVokabel(Vokabel v) {

		entityManager.persist(v);

	}

	public EntityTransaction getEntityTransaction() {

		return entityManager.getTransaction();

	}
}

/*
 * public Kategorie getKategorieByName(String name) throws InvalidNameException
 * { Criteria criteria = session.createCriteria(Kategorie.class); Kategorie
 * kategorie = (Kategorie) criteria.add(Restrictions.eq("name", name))
 * .uniqueResult(); return kategorie; }
 * 
 * public List<Kategorie> getAll(){ return
 * session.createCriteria(Kategorie.class).list(); }
 */
