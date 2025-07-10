package com.keepflower.api.common.util;

import com.keepflower.api.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    /**
     * Initialize the secret key for JWT signing and verification.
     * 
     * @throws IllegalArgumentException if the JWT secret is null or less than 32
     *                                  characters long
     */
    @PostConstruct
    public void init() {
        String jwtSecret = jwtProperties.getSecret();

        if (jwtSecret == null || jwtSecret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }

        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a JWT token that can be used for authentication.
     * 
     * @param sessionId the ID of the session
     * @param userId    the ID of the user
     * @param username  the username of the user
     * @param name      the full name of the user
     * @return a JWT token
     */
    public String generate(UUID sessionId, UUID userId, String username, String name) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.getExpirationSeconds());

        return Jwts.builder()
                .subject(sessionId.toString())
                .claim("userId", userId.toString())
                .claim("username", username)
                .claim("name", name)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Parse the given JWT string and verify its signature using the secret key.
     *
     * @param jwt the JWT string to parse
     * @return the parsed JWT claims
     * @throws JwtException             if the JWT is invalid or tampered with
     * @throws IllegalArgumentException if the JWT is null or empty
     */
    private Jws<Claims> parse(String jwt) throws JwtException, IllegalArgumentException {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt);
    }

    /**
     * Get the payload of a JWT token.
     *
     * @param jwt the JWT token
     * @return the payload of the JWT token
     * @throws JwtException             if the JWT is invalid or tampered with
     * @throws IllegalArgumentException if the JWT is null or empty
     */
    private Claims getPayload(String jwt) throws JwtException, IllegalArgumentException {
        return parse(jwt).getPayload();
    }

    /**
     * Validate the given JWT string.
     * 
     * @param jwt the JWT string to validate
     * @return true if the JWT is valid, false otherwise
     */
    public boolean isValid(@Nullable String jwt) {
        try {
            parse(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get the session ID from the given JWT token.
     * 
     * @param jwt the JWT token
     * @return the session ID
     * @throws JwtException             if the JWT is invalid or tampered with
     * @throws IllegalArgumentException if the JWT is null or empty
     */
    public UUID getSessionId(String jwt) throws JwtException, IllegalArgumentException {
        return UUID.fromString(getPayload(jwt).getSubject());
    }

    /**
     * Get the user ID from the given JWT token.
     * 
     * @param jwt the JWT token
     * @return the user ID
     * @throws JwtException             if the JWT is invalid or tampered with
     * @throws IllegalArgumentException if the JWT is null or empty
     */
    public UUID getUserId(String jwt) throws JwtException, IllegalArgumentException {
        return UUID.fromString(getPayload(jwt).get("userId", String.class));
    }

    /**
     * Get the username from the given JWT token.
     *
     * @param jwt the JWT token
     * @return the username
     * @throws JwtException             if the JWT is invalid or tampered with
     * @throws IllegalArgumentException if the JWT is null or empty
     */
    public String getUsername(String jwt) throws JwtException, IllegalArgumentException {
        return getPayload(jwt).get("username", String.class);
    }

    /**
     * Get the name from the given JWT token.
     *
     * @param jwt the JWT token
     * @return the name
     * @throws JwtException             if the JWT is invalid or tampered with
     * @throws IllegalArgumentException if the JWT is null or empty
     */
    public String getName(String jwt) throws JwtException, IllegalArgumentException {
        return getPayload(jwt).get("name", String.class);
    }
}
