package com.keepflower.api.common.util;

import com.keepflower.api.exception.UnauthorizedException;
import com.keepflower.api.security.token.AuthToken;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@Getter
public class AuthUtil {
    private final Set<String> publicEndpoints = Set.of(
            "/ping",
            "/signup",
            "/login",
            "/auth/refresh",
            "/auth/status");

    public void authenticate(UUID sessionId, UUID userId, String username, String name) {
        SecurityContextHolder.getContext().setAuthentication(new AuthToken(sessionId, userId, username, name));
    }

    public AuthToken getAuthToken() {
        AuthToken authToken = (AuthToken) SecurityContextHolder.getContext().getAuthentication();

        if (authToken == null) {
            throw new UnauthorizedException();
        }

        return authToken;
    }
}
