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

    private final Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256); // Ensure key is properly initialized

    public String generateToken(Authentication auth) {

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        String roles = populateAuthorities(authorities);

        String emailId = auth.getName();

        if (emailId == null || emailId.isEmpty()) {
            throw new IllegalArgumentException("Authentication name (emailId) is null or empty");
        }

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Authorities (roles) are null or empty");
        }

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                .claim("emailId", emailId)
                .claim("authorities", roles)
                .signWith(SignatureAlgorithm.forSigningKey(key), JwtConstant.SECRET_KEY)
                .compact();

        return jwt;
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public String getEmailIdFromJwtToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JwtConstant.SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return String.valueOf(claims.get("emailId"));
    }

    public boolean validateToken(String token){

        try{
            Jwts.parserBuilder().setSigningKey(JwtConstant.SECRET_KEY).build().parseClaimsJws(token);
            return true;
        }
        catch(Exception e){
            throw new AuthenticationCredentialsNotFoundException("Jwt was Expired or incorrect.\n"+e.getMessage());
        }
    }

}