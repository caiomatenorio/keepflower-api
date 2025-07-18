package com.keepflower.api.service;

import com.keepflower.api.common.util.CookieUtil;
import com.keepflower.api.config.properties.JwtProperties;
import com.keepflower.api.config.properties.RefreshTokenProperties;
import com.keepflower.api.config.properties.SessionProperties;
import com.keepflower.api.exception.UserNotFoundException;
import com.keepflower.api.model.Session;
import com.keepflower.api.model.User;
import com.keepflower.api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final CookieUtil cookieUtil;
    private final SessionProperties sessionProperties;
    private final RefreshTokenProperties refreshTokenProperties;
    private final JwtProperties jwtProperties;
    private final UserService userService;
    private final SecureRandom secureRandom;
    private final SessionRepository sessionRepository;

    public ResponseCookie[] createSessionCookies(String jwt, String refreshToken) {
        ResponseCookie access = cookieUtil.createCookie("access_token", jwt, jwtProperties.getExpirationSeconds());
        ResponseCookie refresh = cookieUtil.createCookie("refresh_token", refreshToken,
                sessionProperties.getExpirationSeconds());

        return new ResponseCookie[]{access, refresh};
    }

    public ResponseCookie[] deleteSessionCookies() {
        return Stream.of("access_token", "refresh_token")
                .map(cookieUtil::deleteCookie)
                .toArray(ResponseCookie[]::new);
    }

    private String generateRefreshToken() {
        byte[] randomBytes = new byte[refreshTokenProperties.getLength()];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Transactional
    public Session createSession(String username) throws UserNotFoundException {
        User user = userService.findByUsernameOrThrow(username);
        String refreshToken = generateRefreshToken();

        Session session = new Session();
        session.setUser(user);
        session.setRefreshToken(refreshToken);
        sessionRepository.save(session);
        return session;
    }
}
