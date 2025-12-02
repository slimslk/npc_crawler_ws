package net.dimmid.ws.util;

import io.jsonwebtoken.*;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static final Key key = Keys.hmacShaKeyFor("my-secret-key-my-secret-key".getBytes());

    public static String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 hour
                .signWith(key)
                .compact();
    }

    public static String validateAndGetUserId(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
