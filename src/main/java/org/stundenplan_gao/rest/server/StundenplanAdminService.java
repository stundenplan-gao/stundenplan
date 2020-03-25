package org.stundenplan_gao.rest.server;

import org.stundenplan_gao.jpa.Query;
import org.stundenplan_gao.jpa.database.Fach;
import org.stundenplan_gao.jpa.database.Kurs;
import org.stundenplan_gao.jpa.database.Lehrer;
import org.stundenplan_gao.jpa.database.Schueler;
import org.stundenplan_gao.rest.JWTFilter.JWT;
import org.stundenplan_gao.rest.JWTFilter.JWTAdmin;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/admin")
public class StundenplanAdminService {

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
    @JWTAdmin
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
    public String authenticateAdmin(@QueryParam("username") String username, @QueryParam("password") String password) {

        //if username and password are correct
        if (authenticate(username, password)) {
            //Create a JWT Token that is valid for 10 min. and return it
            return JWT.createJWT("stundenplan", username, 600_000L, true);
        }
        //Return an empty string if the authorization was unsuccessful
        return "";
    }

    private boolean authenticate(String username, String password) {

        //TODO read from config
        //TODO remove hard-coded admin account
        //TODO don't forget
        //TODO remove this huge security flaw
        //TODO this is a bad idea:
        //TODO remove before shipping
        //TODO set an alarm so you don't forget
        //TODO because you will forget to remove this
        String[] admins = {"admin", "admin"};
        String[] passwords = {"password", "admin"};

        boolean authorized = false;
        for (int i = 0; i < admins.length; i++) {
            if (admins[i].equals(username) && passwords[i].equals(password)) {
                authorized = true;
            }
        }

        return authorized;
    }

    //TODO read from config
    private static final boolean confirmationRequired = false;

    @DELETE
    @Path("/schueler/{username}")
    @JWTAdmin
    public Response deleteUser(@PathParam("username") String username) {
        query.deleteUser(username);
        return Response.status(200, "User no longer exists!").build();
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
    @JWTAdmin
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
    @Path("/kurse")
    @Produces({MediaType.APPLICATION_JSON})
    @JWTAdmin
    public Kurs[] getKurse() {
        return query.getAll(Kurs.class);
    }

    @PUT
    @Path("/kurs")
    @Consumes({MediaType.APPLICATION_JSON})
    @JWTAdmin
    public Response addKurs(Kurs kurs) {
        return (query.addObject(kurs) ? Response.status(200) : Response.status(409)).build();
    }

    @DELETE
    @Path("/kurs/{kursId}")
    @JWTAdmin
    public Response deleteKurs(@PathParam("kursId") String kursId) {
        int id;
        try {
            id = Integer.decode(kursId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
        query.deleteKurs(id);
        return Response.status(200).build();
    }

    @GET
    @Path("/lehrer")
    @Produces({MediaType.APPLICATION_JSON})
    @JWTAdmin
    public Lehrer[] getLehrer() {
        return query.getAll(Lehrer.class);
    }

    @PUT
    @Path("/lehrer")
    @Consumes({MediaType.APPLICATION_JSON})
    @JWTAdmin
    public Response addLehrer(Lehrer lehrer) {
        return (query.addObject(lehrer) ? Response.status(200) : Response.status(409)).build();
    }

    @DELETE
    @Path("/lehrer/{lehrerId}")
    @JWTAdmin
    public Response deleteLehrer(@PathParam("lehrerId") String lehrerId) {
        int id;
        try {
            id = Integer.decode(lehrerId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
        query.deleteLehrer(id);
        return Response.status(200).build();
    }

    @GET
    @Path("/schuelerdaten/{benutzername}")
    @Produces({ MediaType.APPLICATION_JSON })
    @JWTAdmin
    public Schueler getSchuelerMitFaechern(@PathParam("benutzername") String benutzername) {
        Schueler schueler = query.getSchueler(benutzername);
        System.err.println(schueler.toFullString());
        //return the user
        return schueler;
    }

    @PUT
    @Path("/schueler")
    @Consumes({MediaType.APPLICATION_JSON})
    @JWTAdmin
    public Response addSchueler(Schueler schueler) {
        return (query.addObject(schueler) ? Response.status(200) : Response.status(409)).build();
    }

    @GET
    @Path("/schueler/all")
    @Produces({MediaType.APPLICATION_JSON})
    @JWTAdmin
    public Schueler[] getSchueler() {
        return query.getAll(Schueler.class);
    }
}
