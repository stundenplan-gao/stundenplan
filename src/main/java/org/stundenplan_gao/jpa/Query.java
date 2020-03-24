package org.stundenplan_gao.jpa;
import org.stundenplan_gao.jpa.database.Fach;
import org.stundenplan_gao.jpa.database.Schueler;
import org.stundenplan_gao.jpa.database.Stufe;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class Query {

    private static EntityManager entityManager;

    public static void setup() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("stundenplan-test");
        entityManager = factory.createEntityManager();
    }

    public static void shutdown() {
        entityManager.close();
    }

    public <T> List<T> query(String queryString, Class<T> resultClass) {
        TypedQuery<T> query = entityManager.createQuery(queryString, resultClass);
        return query.getResultList();
    }

    public <T> T singleResultQuery(String queryString, Class<T> resultClass) {
        TypedQuery<T> query = entityManager.createQuery(queryString, resultClass);
        return query.getSingleResult();
    }


    public void persist(Object obj) {
        entityManager.getTransaction().begin();
        entityManager.persist(obj);
        entityManager.getTransaction().commit();
    }

    public List<Schueler> getSchuelerList(String benutzername) {
        return query("select s from Schueler s where s.benutzername = '" + benutzername.replace("'", "''") + "'", Schueler.class);
    }

    public Schueler getSchueler(String benutzername) {
        List<Schueler> schueler = getSchuelerList(benutzername);

        if (schueler.size() != 1) {
            return null;
        }

        return schueler.get(0);
    }

    public boolean usernameTaken(String benutzername) {
        List<Schueler> schueler = getSchuelerList(benutzername);
        return schueler.size() >= 1;
    }

    public void deleteUser(String benutzername) {
        List<Schueler> schueler = getSchuelerList(benutzername);
        if (schueler != null) {
            entityManager.getTransaction().begin();
            for (Schueler s : schueler) {
                entityManager.remove(s);
            }
            entityManager.getTransaction().commit();
        }
    }

    public Stufe getNullStufe() {
        return singleResultQuery("select s from Stufe s where stufe = null", Stufe.class);
    }
}
