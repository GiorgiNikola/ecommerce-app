package com.ecommerceapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret:myDefaultSecretKeyThatIsLongEnoughForHS256Algorithm}")
    private String SECRET;

    @Value("${jwt.expiration:3600000}")
    private long EXPIRATION; // Default 1 hour

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username, String role) {
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("Error generating JWT token", e);
            throw new RuntimeException("Could not generate token", e);
        }
    }

    public String extractUsername(String token) {
        try {
            return parseClaims(token).getSubject();
        } catch (Exception e) {
            log.error("Error extracting username from token", e);
            throw new JwtException("Invalid token", e);
        }
    }

    public String extractRole(String token) {
        try {
            return parseClaims(token).get("role", String.class);
        } catch (Exception e) {
            log.error("Error extracting role from token", e);
            throw new JwtException("Invalid token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return !isTokenExpired(claims);
        } catch (JwtException e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during token validation", e);
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseClaims(token);
            return isTokenExpired(claims);
        } catch (Exception e) {
            return true;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) throws JwtException {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.debug("JWT token is expired");
            throw new JwtException("Token expired", e);
        } catch (UnsupportedJwtException e) {
            log.debug("JWT token is unsupported");
            throw new JwtException("Unsupported token", e);
        } catch (MalformedJwtException e) {
            log.debug("JWT token is malformed");
            throw new JwtException("Malformed token", e);
        } catch (SecurityException e) {
            log.debug("JWT signature validation failed");
            throw new JwtException("Invalid token signature", e);
        } catch (IllegalArgumentException e) {
            log.debug("JWT token compact of handler are invalid");
            throw new JwtException("Invalid token", e);
        }
    }

    public long getExpirationTime() {
        return EXPIRATION;
    }
}