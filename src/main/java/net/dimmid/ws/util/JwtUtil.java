package net.dimmid.ws.util;

import io.jsonwebtoken.*;

import java.security.Key;
import io.jsonwebtoken.security.Keys;
import net.dimmid.config.Config;

public class JwtUtil {
    private static final Key key;

    static {
        try {
            key = Keys.hmacShaKeyFor(
                    Config.getOrDefault("JWT_TOKEN", "testtesttesttesttesttesttesttest\n").getBytes());
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
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
