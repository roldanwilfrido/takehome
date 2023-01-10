package com.example.takehome.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private TokenUtils() {
    }

    /**
     * Generates a new token using the user credentials
     * @param name Username
     * @param email Email
     * @param validitySeconds Token time life
     * @param secret Secret key
     * @return Token
     */
    public static String generateToken(
            String name, String email, Long validitySeconds, String secret) {

        long expirationTime = validitySeconds * 1_000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put(NAME, name);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .addClaims(claims)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    /**
     * Builds a Username password auth token to be used in the Authentication filter
     * @param token Token
     * @param secret Secret key
     * @return {@link UsernamePasswordAuthenticationToken} object
     */
    public static UsernamePasswordAuthenticationToken getAuthentication(
            String token, String secret) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return new UsernamePasswordAuthenticationToken(
                    claims.getSubject(),
                    null,
                    Collections.emptyList());
        } catch(Exception e) {
            return null;
        }
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    private static final String NAME = "name";

}
