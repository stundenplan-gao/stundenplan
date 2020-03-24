package org.jgrosshardt.rest.JWTFilter;

import io.jsonwebtoken.Claims;
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
@JWTUserAuthNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTUserAuthNeededFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) throws IOException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = context.getHeaderString(HttpHeaders.AUTHORIZATION);

        String path = context.getUriInfo().getPath();
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        String userID = path.substring(path.lastIndexOf('/'));

        //Decode the authorizationHeader and check it
        try {
            Claims claims = JWT.decodeJWT(authorizationHeader);
            if (!userID.equals(claims.get(Claims.SUBJECT))) {
                context.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            }
        } catch (SignatureException e) {
            context.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        } catch (IllegalArgumentException e) {
            context.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
