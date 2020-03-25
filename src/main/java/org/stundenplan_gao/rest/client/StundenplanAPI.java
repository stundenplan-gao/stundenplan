package org.stundenplan_gao.rest.client;

import org.stundenplan_gao.jpa.database.Fach;
import org.stundenplan_gao.jpa.database.NeuerNutzer;
import org.stundenplan_gao.jpa.database.Schueler;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/schueler")
public interface StundenplanAPI {

    @GET
    @Path("/")
    @Produces({ MediaType.TEXT_PLAIN })
    Response index();

    // Test method
    @GET
    @Path("/echo")
    @Produces({ MediaType.TEXT_PLAIN })
    String echo(@QueryParam("message") String message);

    // Test method
    @GET
    @Path("/echo_auth")
    @Produces({ MediaType.TEXT_PLAIN })
    String echoAuth(@QueryParam("message") String message);

    @POST
    @Path("/login")
    @Produces({ MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON })
    String authenticateUser(@QueryParam("username") String username,
                            @QueryParam("password") String password);

    @POST
    @Path("/register")
    @Consumes({MediaType.APPLICATION_JSON})
    Response registerUser(NeuerNutzer nutzer);

    @DELETE
    @Path("/delete/${username}")
    Response deleteUser(@PathParam("username") String username);

    @GET
    @Path("/faecherauswahl")
    @Produces({ MediaType.APPLICATION_JSON })
    Fach[] getFaecherList();

    @GET
    @Path("/schuelerdaten/${benutzername}")
    @Produces({ MediaType.APPLICATION_JSON })
    Schueler getSchuelerMitFaechern(@PathParam("benutzername") String benutzername);
}
