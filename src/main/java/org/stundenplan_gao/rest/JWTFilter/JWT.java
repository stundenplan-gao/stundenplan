package org.stundenplan_gao.rest.JWTFilter;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.Key;
import java.util.Date;

public class JWT {

    //Read the secret key from the private.key file
    private static final String SECRET_KEY;

    static {
        try {
            SECRET_KEY = new String(JWT.class.getResourceAsStream("/private.key").readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Unable to retrieve private key", e);
        }
    }

    //construct a JWT
    public static String createJWT(String issuer, String subject, long ttlMillis, boolean admin) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .claim("admin", admin)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) throws SignatureException {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
