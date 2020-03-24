package org.jgrosshardt.rest.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;

import org.jgrosshardt.rest.JWTFilter.JWT;
import org.jgrosshardt.rest.server.PasswordHash;
import org.junit.Test;

import io.jsonwebtoken.Claims;

public class RestAPIClient {
    
    private static final String ServerURL = "http://localhost";
    
    private static final int port = 8080;

    private Client client;

    private StundenplanClient stundenplanClient;
    
    private Response response;

    private String URL;

    public RestAPIClient() {
        URL = ServerURL + ":" + port + "/Stundenplan_Server/stundenplan/schueler/";
        client = ClientBuilder.newClient();
    }

    public void close() {
        client.close();
    }

    public void getRequest(String path) {
        response = client.target(URL + path).request().get();
    }

    public WebTarget getTarget(String path) {
        return client.target(URL + path);
    }

    @Test
    public void testHash() {
        String password = "abc123";
        String salt = PasswordHash.generateSalt();
        String hash1 = PasswordHash.computeHash(password, salt);
        String hash2 = PasswordHash.computeHash(password, salt);
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

    public WebTarget withTarget(String path, Function<WebTarget, WebTarget> handle) {
        return handle.apply(getTarget(path));
    }
    
    @Test
    public void testEcho() {
        String testMsg = "Dies ist ein Test";
        response = withTarget("echo", target -> {
            return target.queryParam("message", testMsg);
        }).request().header(HttpHeaders.AUTHORIZATION, "").get();

        assertEquals("Expected names of artists does not match", testMsg, response.readEntity(String.class));

        response.close();
    }

    
    @Test
    public void testEchoAuth() {
        String testMsg = "Dies ist ein Test";
        
        // attempt, which fails
        response = withTarget("echo_auth", target -> target.queryParam("message", testMsg)).request().header(HttpHeaders.AUTHORIZATION, "").get();

        assertTrue("Expected status does not match", response.getStatus() != 200);
        assertNotEquals("Expected names of message does not match", testMsg, response.readEntity(String.class));
        
        // login
        Map<String, String> formParams = new HashMap<>();
        formParams.put("username", "ysprenger");
        formParams.put("password", "ysprenger");

        response = withTarget("login", target -> target).request().header(HttpHeaders.AUTHORIZATION, "").post(Entity.json(formParams));

        assertEquals("Expected status does not match", 200, response.getStatus());
        String token = response.readEntity(String.class);

        // attempt, which succeeds
        response = withTarget("echo_auth", target -> target
                .queryParam("message", testMsg)).request().header(HttpHeaders.AUTHORIZATION, token).get();

        assertTrue("Expected status does not match", response.getStatus() == 200);
        assertEquals("Expected names of message does not match", testMsg, response.readEntity(String.class));

        response.close();
    }

    @Test
    public void testUserRegistration() {
        stundenplanClient = new StundenplanClient("", "".toCharArray());

    }
}
