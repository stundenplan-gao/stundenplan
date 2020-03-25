package org.stundenplan_gao.jpa;

import org.stundenplan_gao.jpa.database.Kurs;
import org.stundenplan_gao.jpa.database.Lehrer;
import org.stundenplan_gao.jpa.database.Schueler;
import org.stundenplan_gao.jpa.database.Stufe;

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

    public <T> T[] getAll(Class<T> tClass) {
        List<T> tList = query("select x from " + tClass.getName() + " x", tClass);
        return (T[]) tList.toArray();
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
}
