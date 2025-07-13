package com.keepflower.api.service;

import com.keepflower.api.common.util.CookieUtil;
import com.keepflower.api.config.properties.JwtProperties;
import com.keepflower.api.config.properties.SessionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final CookieUtil cookieUtil;
    private final SessionProperties sessionProperties;
    private final JwtProperties jwtProperties;

    public ResponseCookie[] createSessionCookies(String jwt, String refreshToken) {
        ResponseCookie access = cookieUtil.createCookie("access_token", jwt, jwtProperties.getExpirationSeconds());
        ResponseCookie refresh = cookieUtil.createCookie("refresh_token", refreshToken,
                sessionProperties.getExpirationSeconds());

        return new ResponseCookie[] { access, refresh };
    }

    public ResponseCookie[] deleteSessionCookies() {
        return Stream.of("access_token", "refresh_token")
                .map(cookieUtil::deleteCookie)
                .toArray(ResponseCookie[]::new);
    }
}
