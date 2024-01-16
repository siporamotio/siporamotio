package de.htwberlin.vokabel_manager.impl;

import java.util.List;

import javax.naming.InvalidNameException;

import org.hibernate.Session;

import de.htwberlin.vokabel_manager.api.domain.Bedeutungen;
import de.htwberlin.vokabel_manager.api.domain.Kategorie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BedeutungenDAO {
	private EntityManager em;
	private Session session;
	private static BedeutungenDAO e = null;

	private BedeutungenDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("VokabelDatenbank");
	     em = emf.createEntityManager();
		em.unwrap(org.hibernate.Session.class);;
	}

	public static BedeutungenDAO getBedeutungDao() {
		if (e == null) {
			e = new BedeutungenDAO();
		}
		return e;
	}

	public void saveBedeutung(Bedeutungen Bedeutungen) {
		em.persist(Bedeutungen);
	}

	public void deleteBedeutung(Bedeutungen Bedeutungen) {
		em.remove(Bedeutungen);
	}

	

	public Bedeutungen getBedeutungByDateiname(String dateiname) throws InvalidNameException {
	    CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
	    CriteriaQuery<Bedeutungen> criteriaQuery = criteriaBuilder.createQuery(Bedeutungen.class);
	    Root<Bedeutungen> root = criteriaQuery.from(Bedeutungen.class);

	    criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("bezeichnung"), dateiname));

	    return session.createQuery(criteriaQuery).uniqueResult();
	}
	public Bedeutungen getById(long id) {
		return em.find(Bedeutungen.class, id);
	}

	public List<Bedeutungen> getByKategorie(Kategorie k) {
	    CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
	    CriteriaQuery<Bedeutungen> criteriaQuery = criteriaBuilder.createQuery(Bedeutungen.class);
	    Root<Bedeutungen> root = criteriaQuery.from(Bedeutungen.class);

	    // Fügen Sie die Bedingung hinzu: WHERE kategorie = :kategorie
	    Predicate predicate = criteriaBuilder.equal(root.get("kategorie"), k);
	    criteriaQuery.where(predicate);

	    // Führen Sie die Abfrage aus und erhalten Sie die Ergebnisliste
	    return session.createQuery(criteriaQuery).getResultList();
	}
	
	public EntityTransaction getEntityTransaction() {
		return em.getTransaction();
	}
}
