package com.panilya.redditserver.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
public final class JwtProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final Map<String, Object> roles = new HashMap<>();

    public String generateJwtToken(String username) {
        roles.put("roles", "ROLE_USER");
        return Jwts.builder().setSubject(username).setClaims(roles).signWith(key).compact();
    }

    public boolean validateToken(final String token) {
        Claims claims = getClaims(token);
        if (claims.isEmpty()) {
            return false;
        }
        return true;
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}
