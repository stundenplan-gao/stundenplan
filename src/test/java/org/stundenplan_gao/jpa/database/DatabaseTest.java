package org.stundenplan_gao.jpa.database;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.stundenplan_gao.rest.server.PasswordHash;

public class DatabaseTest {

    private static EntityManager entityManager;

    @BeforeClass
    public static void setup() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("stundenplan-test");
        entityManager = factory.createEntityManager();
        Map<String, Object> properties = factory.getProperties();
        for (String s: properties.keySet()) {
            System.err.println(s + " : " + properties.get(s));
        }
        // Employee employee = new Employee();
        // employee.setEmpNumber("hans");
        // employee.setAge(15);
        // employee.setName("Hans");
        // entityManager.getTransaction().begin();
        // entityManager.persist(employee);
        // entityManager.getTransaction().commit();
    }

    @AfterClass
    public static void shutdown() {
        entityManager.close();
    }

    @Test
    public void testStufen() {
        TypedQuery<Stufe> stufen = entityManager.createQuery("select s From Stufe s", Stufe.class);
        List<Stufe> results = stufen.getResultList();
        for (Stufe stufe : results) {
            System.err.println(stufe);
        }
    }

    @Test
    public void testLehrer() {
        TypedQuery<Lehrer> lehrer = entityManager.createQuery("select l From Lehrer l", Lehrer.class);
        List<Lehrer> results = lehrer.getResultList();
        for (Lehrer l : results) {
            System.err.println(l);
        }
    }

    @Test
    public void testSchueler() {
        TypedQuery<Schueler> schueler = entityManager.createQuery("select s From Schueler s", Schueler.class);
        List<Schueler> results = schueler.getResultList();
        for (Schueler s : results) {
            System.err.println(s);
            if (s.getId() == 100) {
                entityManager.getTransaction().begin();
                entityManager.remove(s);
                entityManager.getTransaction().commit();
            }
        }

        TypedQuery<Stufe> stufen = entityManager.createQuery("select s From Stufe s Where s.stufe = 'EF'", Stufe.class);

        Schueler s = new Schueler();
        s.setVorname("Justus");
        s.setNachname("Groß-Hardt");
        s.setBenutzername("jgrosshardt");
        String salt = PasswordHash.generateSalt();
        s.setPasswortHash(PasswordHash.computeHash("12345", salt));
        s.setSalt(salt);
        s.setStufe(stufen.getSingleResult());

        entityManager.getTransaction().begin();
        entityManager.persist(s);
        entityManager.getTransaction().commit();

        System.err.println(s);

        entityManager.getTransaction().begin();
        entityManager.remove(s);
        entityManager.getTransaction().commit();
    }

    @Test
    public void testKurse() {
        TypedQuery<Kurs> schueler = entityManager.createQuery("select k From Kurs k", Kurs.class);
        List<Kurs> results = schueler.getResultList();
        for (Kurs k : results) {
            System.err.println(k);
        }
    }

    @Test
    public void testStunden() {
        TypedQuery<Stunde> schueler = entityManager.createQuery("select s From Stunde s", Stunde.class);
        List<Stunde> results = schueler.getResultList();
        for (Stunde s : results) {
            System.err.println(s);
        }
    }
}
