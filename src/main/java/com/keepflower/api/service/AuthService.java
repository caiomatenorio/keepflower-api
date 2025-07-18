package com.keepflower.api.service;

import com.keepflower.api.common.token.AuthToken;
import com.keepflower.api.common.util.JwtUtil;
import com.keepflower.api.exception.InvalidCredentialsException;
import com.keepflower.api.exception.UnauthorizedException;
import com.keepflower.api.exception.UserNotFoundException;
import com.keepflower.api.model.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final SessionService sessionService;
    private final JwtUtil jwtUtil;

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

    @Transactional
    public ResponseCookie[] login(String username, String password) throws InvalidCredentialsException {
        Session session;

        if (!userService.validateCredentials(username, password)) {
            throw new InvalidCredentialsException();
        }

        try {
            session = sessionService.createSession(username);
        } catch (UserNotFoundException e) {
            throw new InvalidCredentialsException();
        }

        String jwt = jwtUtil.generate(session.getId(), session.getUser().getId(), session.getUser().getUsername());
        return sessionService.createSessionCookies(jwt, session.getRefreshToken());
    }
}
