package org.stundenplan_gao;

import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.stundenplan_gao.jpa.database.*;
import org.stundenplan_gao.rest.JWTFilter.JWT;
import org.stundenplan_gao.rest.client.StundenplanClient;
import org.stundenplan_gao.rest.server.PasswordHash;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class AllInOneTest {

    private EntityManager entityManager;

    private final int port = 8080;

    private StundenplanClient client;

    private String URL = "http://localhost:8080/stundenplan_server/stundenplan";

    public AllInOneTest() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("stundenplan-test");
        entityManager = factory.createEntityManager();

        client = new StundenplanClient("", "".toCharArray(), URL);
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
        s.setBenutzername("stundenplan_gao");
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



    @Test
    public void testHash() {
        String password = "abc123";
        String salt = PasswordHash.generateSalt();
        String hash1 = PasswordHash.computeHash(password, salt);
        String hash2 = PasswordHash.computeHash(password, PasswordHash.bytesToBase64(PasswordHash.base64ToBytes(salt)));
        assertEquals(hash1, hash2);
        System.out.println(hash1);
        assertEquals(salt, PasswordHash.bytesToBase64(PasswordHash.base64ToBytes(salt)));
        System.out.println(salt);
    }

    @Test
    public void testJWT() {
        String token = JWT.createJWT("hjklhjhkj", "kuiopoiop", 30_000L, true);
        Claims claims = JWT.decodeJWT(token);
        claims.forEach((k, v) -> {
            System.err.println(k + "=>" + v);
        });
    }

    @Test
    public void testEcho() {
        String testMsg = "Message";
        String response = client.echo(testMsg);
        assertEquals("Message does not match", testMsg, response);
    }

    private static String benutzername = "justus.gross-hardt@gao-online.de";
    private static String passwort = "123";

    @Test
    public void testEchoAuth() {
        String testMsg = "Dies ist ein Test";

        // attempt, which fails
        String response = client.echoAuth(testMsg);

        assertNotEquals("Message does not match", testMsg, response);

        Response resp = client.registerUser(new NeuerNutzer("Justus", "Groß-Hardt", benutzername, passwort));
        System.err.println(resp.getStatus() + ": " + resp.getStatusInfo().getReasonPhrase());
        resp.close();

        String token = client.login(benutzername, passwort.toCharArray());
        assertFalse("Login failed", token == null || token.equals(""));

        client.setToken(token);

        response = client.echoAuth(testMsg);
        assertEquals("Message does not match", testMsg, response);
    }

    @Test
    public void testDelete() {
        client.deleteUser(benutzername);
        assertTrue(client.login(benutzername, passwort.toCharArray()).equals(""));
    }
}
