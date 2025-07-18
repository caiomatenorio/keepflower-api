package com.keepflower.api.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.UUID;

@Getter
public class AuthToken extends UsernamePasswordAuthenticationToken {
    private final UUID sessionId;
    private final UUID userId;
    private final String username;

    public AuthToken(UUID sessionId, UUID userId, String username) {
        super(username, null, List.of());

        this.sessionId = sessionId;
        this.userId = userId;
        this.username = username;
    }
}
