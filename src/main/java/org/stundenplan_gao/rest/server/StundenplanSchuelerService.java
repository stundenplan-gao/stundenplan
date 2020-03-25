package org.stundenplan_gao.rest.server;

import org.stundenplan_gao.jpa.Query;
import org.stundenplan_gao.jpa.database.*;
import org.stundenplan_gao.rest.JWTFilter.JWT;
import org.stundenplan_gao.rest.JWTFilter.JWTToken;
import org.stundenplan_gao.rest.JWTFilter.JWTUsername;
import org.stundenplan_gao.rest.client.StundenplanAPI;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/schueler")
public class StundenplanSchuelerService implements StundenplanAPI {

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
    @JWTToken
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
            return JWT.createJWT("stundenplan", username, 600_000L, false);
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
        if (!nutzer.getBenutzername().endsWith("@gao-online.de")) {
            return Response.status(420, "Invalid email address!").build();
        }
        if (query.usernameTaken(nutzer.getBenutzername())) {
            return Response.status(422, "Username already taken!").build();
        }
        if (confirmationRequired) {
            Unbestaetigt user = new Unbestaetigt(nutzer);
            query.addObject(user);
        } else {
            Stufe empty = query.getNullStufe();
            Schueler schueler = new Schueler(nutzer, empty);
            query.addObject(schueler);
        }
        return Response.status(200).build();
    }

    @DELETE
    @Path("/delete/{username}")
    @JWTUsername
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
    @Path("/schuelerdaten/{benutzername}")
    @Produces({ MediaType.APPLICATION_JSON })
    @JWTUsername
    public Schueler getSchuelerMitFaechern(@PathParam("benutzername") String benutzername) {

        Schueler schueler = query.getSchueler(benutzername);
        System.err.println(schueler.toFullString());
        // return the user
        return schueler;
    }

    @PUT
    @Path("/schuelerdaten/{benutzername}")
    @Consumes({MediaType.APPLICATION_JSON})
    @JWTUsername
    public Response storeSchuelerdaten(@PathParam("benutzername") String benutzername, Schueler schueler) {
        return (query.updateSchueler(schueler) ? Response.status(200) : Response.status(404)).build();
    }

    @PUT
    @Path("/schuelerkurse/{benutzername}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @JWTUsername
    public Response storeSchuelerKurse(@PathParam("benutzername") String benutzername, Kurs[] kurse) {
        return (query.updateSchueler(kurse, benutzername) ? Response.status(200) : Response.status(404)).build();
    }

    @GET
    @Path("/kurse")
    @Produces({MediaType.APPLICATION_JSON})
    @JWTToken
    public Kurs[] getKurse() {
        return query.getAll(Kurs.class);
    }

    @GET
    @Path("/vertretungsplan")
    @Produces({ MediaType.APPLICATION_JSON })
    @JWTToken
    public Entfall[] getEntfaelle() {
        return query.getAll(Entfall.class);
    }

    @PUT
    @Path("/changepassword/{benutzername}")
    @Consumes({MediaType.APPLICATION_JSON})
    @JWTUsername
    public Response changePassword(@PathParam("benutzername") String benutzername, String password) {
        if (!query.usernameTaken(benutzername)) {
            return Response.status(404).build();
        }
        String salt = PasswordHash.generateSalt();
        String passwordHash = PasswordHash.computeHash(new String(password), salt);
        query.changePassword(benutzername, passwordHash, salt);
        return Response.status(200).build();
    }


    @GET
    @Path("/lehrer")
    @Produces({MediaType.APPLICATION_JSON})
    @JWTToken
    public Lehrer[] getLehrer() {
        return query.getAll(Lehrer.class);
    }

    @GET
    @Path("/stufen")
    @Produces({MediaType.APPLICATION_JSON})
    @JWTToken
    public Stufe[] getStufen() {
        return query.getAll(Stufe.class);
    }
}
