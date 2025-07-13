package com.keepflower.api.common.util;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Consumer;

@Component
public class CookieUtil {
    private final boolean isProd;

    public CookieUtil(Environment env) {
        isProd = Arrays.asList(env.getActiveProfiles()).contains("prod");
    }

    private ResponseCookie buildCookie(String name, String value, int maxAge) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Cookie name cannot be empty");
        }

        return ResponseCookie.from(name, value)
                .maxAge(maxAge)
                .httpOnly(true)
                .secure(isProd)
                .path("/")
                .sameSite("None")
                .build();
    }

    public ResponseCookie createCookie(String name, String value, int maxAge) throws IllegalArgumentException {
        return buildCookie(name, value, maxAge);
    }

    public ResponseCookie deleteCookie(String name) throws IllegalArgumentException {
        return buildCookie(name, "", 0);
    }

    public Consumer<HttpHeaders> cookiesToHeadersConsumer(ResponseCookie... cookies) {
        return headers -> Arrays.stream(cookies)
                .forEach(cookie -> headers.add(HttpHeaders.SET_COOKIE, cookie.toString()));
    }
}
