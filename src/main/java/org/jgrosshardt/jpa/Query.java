package org.jgrosshardt.jpa;

import org.jgrosshardt.jpa.database.Fach;
import org.jgrosshardt.jpa.database.Schueler;
import org.jgrosshardt.jpa.database.Unbestaetigt;

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

    public <T> List<T> query(String qlString, Class<T> resultClass) {
        TypedQuery<T> query = entityManager.createQuery(qlString, resultClass);
        return query.getResultList();
    }

    //stores an object to the database
    public void persist(Object obj) {
        entityManager.getTransaction().begin();
        entityManager.persist(obj);
        entityManager.getTransaction().commit();
    }

    public static void main(String[] args) {
        setup();
        Query q = new Query();
        List<Schueler> list = q.query("select s from Schueler s", Schueler.class);
        System.out.println(list.get(0));
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

    public boolean usernameTaken(String benutzername) {
        List<Schueler> schueler = query(
                "select s from Schueler s where s.benutzername = '" + benutzername.replace("'", "''") + "'",
                Schueler.class);
        List<Unbestaetigt> unconfirmed = query(
                "select s from Unbestaetigt u where u.benutzername = '" + benutzername.replace("'", "''") + "'",
                Unbestaetigt.class);
        return schueler.size() + unconfirmed.size() >= 1;
    }

    public Unbestaetigt getUnbestaetigt(String benutzername) {
        List<Unbestaetigt> user = query(
                "select s from Unbestaetigt u where u.benutzername = '" + benutzername.replace("'", "''") + "'",
                Unbestaetigt.class);
        if (user.size() != 1) {
            return null;
        }
        return user.get(0);
    }
}
