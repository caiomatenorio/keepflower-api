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
            "/auth/status",
            "/auth/signup",
            "/auth/login",
            "/auth/refresh");

    /**
     * Set the given session ID, user ID, username, and name as the authentication
     * data.
     *
     * @param sessionId the session ID to set
     * @param userId    the user ID to set
     * @param username  the username to set
     * @param name      the name to set
     */
    public void authenticate(UUID sessionId, UUID userId, String username, String name) {
        SecurityContextHolder.getContext().setAuthentication(new AuthToken(sessionId, userId, username, name));
    }

    /**
     * Gets the current authentication token.
     *
     * @return the current authentication token
     * @throws UnauthorizedException if the authentication token is null
     */
    public AuthToken getAuthToken() {
        AuthToken authToken = (AuthToken) SecurityContextHolder.getContext().getAuthentication();

        if (authToken == null) {
            throw new UnauthorizedException();
        }

        return authToken;
    }
}
