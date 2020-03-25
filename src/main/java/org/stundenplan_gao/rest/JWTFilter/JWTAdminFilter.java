package org.stundenplan_gao.rest.JWTFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@JWTToken
@Priority(Priorities.AUTHENTICATION)
public class JWTAdminFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) throws IOException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = context.getHeaderString(HttpHeaders.AUTHORIZATION);

        //Decode the authorizationHeader and check it
        try {
            Claims claims = JWT.decodeJWT(authorizationHeader);
            if (!(boolean) claims.get("admin")) {
                context.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            }
        } catch (SignatureException e) {
            context.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        } catch (IllegalArgumentException e) {
            context.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}