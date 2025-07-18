package com.keepflower.api.common.util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.keepflower.api.config.properties.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        String jwtSecret = jwtProperties.getSecret();

        if (jwtSecret == null || jwtSecret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }

        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generate(UUID sessionId, UUID userId, String username) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.getExpirationSeconds());

        return Jwts.builder()
                .subject(sessionId.toString())
                .claim("userId", userId.toString())
                .claim("username", username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    private Jws<Claims> parse(String jwt) throws JwtException, IllegalArgumentException {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt);
    }

    private Claims getPayload(String jwt) throws JwtException, IllegalArgumentException {
        return parse(jwt).getPayload();
    }

    public boolean isValid(@Nullable String jwt) {
        try {
            parse(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UUID getSessionId(String jwt) throws JwtException, IllegalArgumentException {
        return UUID.fromString(getPayload(jwt).getSubject());
    }

    public UUID getUserId(String jwt) throws JwtException, IllegalArgumentException {
        return UUID.fromString(getPayload(jwt).get("userId", String.class));
    }

    public String getUsername(String jwt) throws JwtException, IllegalArgumentException {
        return getPayload(jwt).get("username", String.class);
    }
}
