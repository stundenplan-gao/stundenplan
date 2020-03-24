package org.stundenplan_gao.rest.server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.stundenplan_gao.jpa.Query;
import org.stundenplan_gao.jpa.database.Fach;
import org.stundenplan_gao.jpa.database.NeuerNutzer;
import org.stundenplan_gao.jpa.database.Schueler;
import org.stundenplan_gao.jpa.database.Unbestaetigt;
import org.stundenplan_gao.rest.JWTFilter.JWT;
import org.stundenplan_gao.rest.JWTFilter.JWTTokenNeeded;

import java.util.List;

@Path("/schueler")
public class StundenplanSchuelerService {

    static {
        Query.setup();
    }
    private Query query = new Query();

    @GET
    @Path("/")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response index() {
        return Response.status(200).header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .entity("").build();
    }

    //Test method
    @GET
    @Path("/echo")
    @Produces({ MediaType.TEXT_PLAIN })
    public String echo(@QueryParam("message") String message) {
        return (message != null ? message : "No message!");
    }

    //Test method
    @GET
    @Path("/echo_auth")
    @Produces({ MediaType.TEXT_PLAIN })
    @JWTTokenNeeded
    public String echoAuth(@QueryParam("message") String message) {
        return (message != null ? message : "No message!");
    }

    /**
     * authenticateUser takes username and password of an existing user
     * and if username and password are correct returns a temporary JWT Token
     * to the client
     *
     * @param username the username of the user
     * @param password the users password
     * @return the JWT Token
     */
    @POST
    @Path("/login")
    @Produces({ MediaType.TEXT_PLAIN })
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    public String authenticateUser(@QueryParam("username") String username, @QueryParam("password") String password) {

        //if username and password are correct
        if (authenticate(username, password)) {
            //Create a JWT Token that is valid for 10 min. and return it
            return JWT.createJWT("stundenplan", username, 600_000L, true);
        }
        //Return an empty string if the authorization was unsuccessful
        return "";
    }

    private boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        Schueler user = query.getSchueler(username);
        if (user == null) {
            return false;
        }

        String salt = user.getSalt();
        return user.getPasswortHash().equals(PasswordHash.computeHash(password, salt));
    }

    //TODO read from config
    private static final boolean confirmationRequired = false;

    @POST
    @Path("/register")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response registerUser(NeuerNutzer nutzer) {
        if (confirmationRequired) {
            Unbestaetigt user = new Unbestaetigt(nutzer);
            query.persist(user);
        } else {
            Schueler schueler = new Schueler(nutzer);
            query.persist(schueler);
        }
        return null;
    }

    /**
     * getFaecherList is available with a get request at "/faecherauswahl"
     * and requires authentication with a JWT Token.
     * It returns all subjects stored in the database as JSON
     *
     * @return an array with all subjects in the database
     */
    @GET
    @Path("/faecherauswahl")
    @Produces({ MediaType.APPLICATION_JSON })
    //@JWTTokenNeeded
    public Fach[] getFaecherList() {
        //Retrieve a List of all subjects from the database
        List<Fach> results = query.query("select f from Fach f", Fach.class);
        //Convert the List to an Array
        int length = results.size();
        Fach[] faecher = new Fach[length];
        for (int i = 0; i < length; i++) {
            faecher[i] = results.get(i);
        }
        //return the array
        return faecher;
    }

    @GET
    @Path("/schueler-mit-faechern/${benutzername}")
    @Produces({ MediaType.APPLICATION_JSON })
    @JWTTokenNeeded
    public Schueler getSchuelerMitFaechern(@PathParam("benutzername") String benutzername) {

        Schueler schueler = query.getSchueler(benutzername);
        System.err.println(schueler.toFullString());
        // return the user
        return schueler;
    }
}
