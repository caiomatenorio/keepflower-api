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

    /**
     * Build a ResponseCookie given a name, value, and max age.
     *
     * The cookie will have the following properties:
     * - HTTP only
     * - Secure if the application is running in production
     * - Path of '/'
     * - Same site of 'None'
     *
     * @param name   the name of the cookie
     * @param value  the value of the cookie
     * @param maxAge the max age of the cookie in seconds
     * @return a ResponseCookie
     * @throws IllegalArgumentException if the cookie name is empty
     */
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

    /**
     * Creates a ResponseCookie that can be used to set a cookie in the
     * client's browser.
     *
     * @param name   the name of the cookie
     * @param value  the value of the cookie
     * @param maxAge the maximum age of the cookie in seconds
     * @return a ResponseCookie
     * @throws IllegalArgumentException if the cookie name is empty
     */
    public ResponseCookie createCookie(String name, String value, int maxAge) throws IllegalArgumentException {
        return buildCookie(name, value, maxAge);
    }

    /**
     * Creates a ResponseCookie that can be used to delete a cookie in the
     * client's browser.
     *
     * @param name the name of the cookie to delete
     * @return a ResponseCookie
     * @throws IllegalArgumentException if the cookie name is empty
     */
    public ResponseCookie deleteCookie(String name) throws IllegalArgumentException {
        return buildCookie(name, "", 0);
    }

    /**
     * Returns a Consumer that adds the given cookies to the HttpHeaders.
     * Each cookie is added as a 'Set-Cookie' header.
     *
     * @param cookies the array of ResponseCookie objects to be added to the headers
     * @return a Consumer that can be used to add the specified cookies to
     *         HttpHeaders
     */

    public Consumer<HttpHeaders> cookiesToHeadersConsumer(ResponseCookie... cookies) {
        return headers -> Arrays.stream(cookies)
                .forEach(cookie -> headers.add(HttpHeaders.SET_COOKIE, cookie.toString()));
    }
}
