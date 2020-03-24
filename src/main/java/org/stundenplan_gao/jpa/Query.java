package org.stundenplan_gao.jpa;
import org.stundenplan_gao.jpa.database.Fach;
import org.stundenplan_gao.jpa.database.Schueler;

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

    public void persist(Object obj) {
        entityManager.getTransaction().begin();
        entityManager.persist(obj);
        entityManager.getTransaction().commit();
    }

    public static void main(String[] args) {
        setup();
        Query q = new Query();
        List<Fach> list = q.query("select f from Fach f", Fach.class);
        System.out.println(list.get(1));
        shutdown();
    }

    public Schueler getSchueler(String benutzername) {
        List<Schueler> schueler = query(
                "select s from Schueler s where s.benutzername = '" + benutzername.replace("'", "''") + "'",
                Schueler.class);

        if (schueler.size() != 1) {
            return null;
        }
        return schueler.get(0);
    }
}
