package com.keepflower.api.security;

import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.keepflower.api.exception.UnauthorizedException;

import lombok.Getter;

@Component
@Getter
public class AuthUtil {
	private final Set<String> publicPaths = Set.of("/ping", "/signup", "/login", "/auth/refresh", "/auth/status");

	public void authenticate(UUID sessionId, UUID userId, String username) {
		SecurityContextHolder.getContext().setAuthentication(new AuthToken(sessionId, userId, username));
	}

	public AuthToken getAuthToken() {
		AuthToken authToken = (AuthToken) SecurityContextHolder.getContext().getAuthentication();

		if (authToken == null) {
			throw new UnauthorizedException();
		}

		return authToken;
	}
}
