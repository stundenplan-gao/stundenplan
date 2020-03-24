package org.jgrosshardt.rest.JWTFilter;

import io.jsonwebtoken.SignatureException;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import javax.annotation.Priority;
import java.io.IOException;

@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) throws IOException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = context.getHeaderString(HttpHeaders.AUTHORIZATION);

        //Decode the authorizationHeader and check it
        try {
            JWT.decodeJWT(authorizationHeader);
        } catch (SignatureException e) {
            context.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        } catch (IllegalArgumentException e) {
            context.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
