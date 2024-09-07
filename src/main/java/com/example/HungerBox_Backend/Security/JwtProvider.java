package com.example.HungerBox_Backend.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    // Secret key for signing JWT tokens
    private final Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256); // Ensure key is properly initialized

    /**
     * Generates a JWT token based on the authentication object.
     * @param auth the authentication object containing user details and authorities.
     * @return the generated JWT token as a String.
     */
    public String generateToken(Authentication auth) {

        // Extract authorities and email from the authentication object
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        String roles = populateAuthorities(authorities);

        String emailId = auth.getName();

        // Validate emailId and roles
        if (emailId == null || emailId.isEmpty()) {
            throw new IllegalArgumentException("Authentication name (emailId) is null or empty");
        }

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Authorities (roles) are null or empty");
        }
        // Build and return JWT token
        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                .claim("emailId", emailId)
                .claim("authorities", roles)
                .signWith(SignatureAlgorithm.forSigningKey(key), JwtConstant.SECRET_KEY)
                .compact();

        return jwt;
    }

    /**
     * Converts a collection of granted authorities to a comma-separated string.
     *
     * @param authorities the collection of granted authorities.
     * @return a comma-separated string of authorities.
     */
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * Extracts the email ID from the JWT token.
     *
     * @param token the JWT token.
     * @return the email ID extracted from the token.
     */
    public String getEmailIdFromJwtToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JwtConstant.SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return String.valueOf(claims.get("emailId"));
    }

    /**
     * Validates the JWT token.
     * @param token the JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token){

        try{
            Jwts.parserBuilder().setSigningKey(JwtConstant.SECRET_KEY)  // Set the key used for signing the token
                    .build()
                    .parseClaimsJws(token); // Parse and validate the token
            return true;
        }
        catch(Exception e){
            // Throw exception if token is invalid or expired
            throw new AuthenticationCredentialsNotFoundException("Jwt was Expired or incorrect.\n"+e.getMessage());
        }
    }

}