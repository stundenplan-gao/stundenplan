package org.stundenplan_gao.jpa;

import org.stundenplan_gao.jpa.database.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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


    public boolean addObject(Object obj) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(obj);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Schueler> getSchuelerList(String benutzername) {
        return query("select s from Schueler s where s.benutzername = '" + benutzername.replace("'", "''") + "'", Schueler.class);
    }

    public List<Unbestaetigt> getUnbestaetigtList(String benutzername) {
        return query("select s from Unbestaetigt u where u.benutzername = '" + benutzername.replace("'", "''") + "'", Unbestaetigt.class);
    }

    public Schueler getSchueler(String benutzername) {
        List<Schueler> schueler = getSchuelerList(benutzername);

        if (schueler.size() != 1) {
            return null;
        }

        return schueler.get(0);
    }

    public Unbestaetigt getUnbestaetigt(String benutzername) {
        List<Unbestaetigt> unbestaetigt = getUnbestaetigtList(benutzername);

        if (unbestaetigt.size() != 1) {
            return null;
        }

        return unbestaetigt.get(0);
    }

    public boolean usernameTaken(String benutzername) {
        List<Schueler> schueler = getSchuelerList(benutzername);
        List<Unbestaetigt> unbestaetigt = query("select u from Unbestaetigt u where u.benutzername = '" + benutzername.replace("'", "''") + "'", Unbestaetigt.class);
        return schueler.size() + unbestaetigt.size() >= 1;
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

    public <T> T[] getAll(Class<T> tClass, T[] tArray) {
        List<T> tList = query("select x from " + tClass.getName() + " x", tClass);
        return tList.toArray(tArray);
    }

    public void deleteKurs(int id) {
        List<Kurs> kurse = query("select Kurs k from k where id = " + id, Kurs.class);
        if (kurse != null) {
            entityManager.getTransaction().begin();
            for (Kurs k : kurse) {
                entityManager.remove(k);
            }
            entityManager.getTransaction().commit();
        }
    }

    public void deleteLehrer(int id) {
        List<Lehrer> lehrer = query("select Lehrer l from l where id = " + id, Lehrer.class);
        if (lehrer != null) {
            entityManager.getTransaction().begin();
            for (Lehrer l : lehrer) {
                entityManager.remove(l);
            }
            entityManager.getTransaction().commit();
        }
    }

    public Kurs[] ausfallendeKurse() {
        List<Kurs> tList = query("select k from Kurs k where id = ", Kurs.class);
        return (Kurs[]) tList.toArray();
    }

    public boolean updateSchueler(Kurs[] kurse, String username) {
        entityManager.getTransaction().begin();
        Schueler s = getSchueler(username);
        if (s == null) {
            entityManager.getTransaction().rollback();
            return false;
        }
        Set<Kurs> kursSet = s.getKurse();
        List<Kurs> kursList = Arrays.asList(kurse);
        kursSet.clear();
        kursSet.addAll(kursList);
        entityManager.getTransaction().commit();
        return true;
    }

    public void changePassword(String benutzername, String passwordHash, String salt) {
        entityManager.getTransaction().begin();
        Schueler schueler = getSchueler(benutzername);
        if (schueler == null) {
            entityManager.getTransaction().rollback();
            entityManager.getTransaction().begin();
            Unbestaetigt unbestaetigt = getUnbestaetigt(benutzername);
            unbestaetigt.setPasswortHash(passwordHash);
            unbestaetigt.setSalt(salt);
            entityManager.getTransaction().commit();
            return;
        }
        schueler.setPasswortHash(passwordHash);
        schueler.setSalt(salt);
        entityManager.getTransaction().commit();
        return;
    }

    public boolean updateSchueler(Schueler schueler) {
        entityManager.getTransaction().begin();
        Schueler s = getSchueler(schueler.getBenutzername());
        if (s == null) {
            entityManager.getTransaction().rollback();
            return false;
        }
        s.setVorname(schueler.getVorname());
        s.setNachname(schueler.getNachname());
        s.setStufe(schueler.getStufe());
        entityManager.getTransaction().commit();
        return true;
    }
}
