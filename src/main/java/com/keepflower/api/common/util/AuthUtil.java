package com.keepflower.api.common.util;

import com.keepflower.api.common.exception.UnauthorizedException;
import com.keepflower.api.security.token.AuthToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthUtil {
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
