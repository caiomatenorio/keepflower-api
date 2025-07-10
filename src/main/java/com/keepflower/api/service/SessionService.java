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

    /**
     * Create the session cookies.
     *
     * @param jwt          the JWT token used for accessing resources
     * @param refreshToken the refresh token used for refreshing the JWT token
     * @return the ResponseCookies for the session
     */
    public ResponseCookie[] createSessionCookies(String jwt, String refreshToken) {
        ResponseCookie access = cookieUtil.createCookie("access_token", jwt, jwtProperties.getExpirationSeconds());
        ResponseCookie refresh = cookieUtil.createCookie("refresh_token", refreshToken,
                sessionProperties.getExpirationSeconds());

        return new ResponseCookie[] { access, refresh };
    }

    /**
     * Delete the session cookies.
     *
     * This method returns ResponseCookies that can be used to remove the
     * "access_token" and "refresh_token" cookies from the client's browser.
     *
     * @return an array of ResponseCookies for deleting the session cookies
     */
    public ResponseCookie[] deleteSessionCookies() {
        return Stream.of("access_token", "refresh_token")
                .map(cookieUtil::deleteCookie)
                .toArray(ResponseCookie[]::new);
    }
}
