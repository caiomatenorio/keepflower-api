package com.keepflower.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepflower.api.common.error_code.ErrorCode;
import com.keepflower.api.common.response.ErrorResponseBody;
import com.keepflower.api.common.response.ResponseBody;
import com.keepflower.api.common.util.AuthUtil;
import com.keepflower.api.common.util.JwtUtil;
import com.keepflower.api.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AuthUtil authUtil;
    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    /**
     * Performs the actual authentication logic.
     * 
     * @param request     the current HTTP request
     * @param response    the current HTTP response
     * @param filterChain the filter chain to continue with after this filter is
     *                    done
     * @throws ServletException      if there was an unexpected problem with the
     *                               filter.
     * @throws IOException           if there was an unexpected problem with the
     *                               filter.
     * @throws UnauthorizedException if the JWT is invalid or tampered with
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException, UnauthorizedException {
        String jwt = getJwtFromRequest(request);

        if (!jwtUtil.isValid(jwt)) {
            ErrorCode errorCode = ErrorCode.E001;
            ErrorResponseBody responseBody = new ErrorResponseBody(errorCode.getMessage(messageSource), errorCode);
            writeResponse(response, errorCode.getStatusCode(), responseBody);
            return;
        }

        authenticate(jwt);
        filterChain.doFilter(request, response);
    }

    /**
     * Returns true if the request should not be filtered by this filter.
     * 
     * By default, this filter will filter all requests. This method can be
     * overridden to filter only specific requests.
     * 
     * @param request the current HTTP request
     * @return true if the request should not be filtered, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        return authUtil.getPublicEndpoints().contains(path);
    }

    /**
     * Extracts the JWT from the given HTTP request.
     * 
     * The JWT is expected to be in a cookie named "access_token".
     * 
     * @param request the HTTP request
     * @return the JWT if found, null otherwise
     */
    private @Nullable String getJwtFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals("access_token"))
                        .findFirst())
                .map(Cookie::getValue)
                .orElse(null);
    }

    /**
     * Writes the given response body to the given HTTP response with the given
     * status code.
     * 
     * @param response     the HTTP response
     * @param statusCode   the status code to write
     * @param responseBody the response body to write
     * @throws IOException if there was an unexpected problem writing to the
     *                     response
     */
    private void writeResponse(
            HttpServletResponse response,
            int statusCode,
            ResponseBody responseBody) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), responseBody);
    }

    /**
     * Authenticates the user with the given JWT token.
     * 
     * Extracts the session ID, user ID, username, and name from the JWT token
     * and sets them as the authentication data.
     * 
     * @param jwt the JWT token
     */
    private void authenticate(String jwt) {
        UUID sessionId = jwtUtil.getSessionId(jwt);
        UUID userId = jwtUtil.getUserId(jwt);
        String username = jwtUtil.getUsername(jwt);
        String name = jwtUtil.getName(jwt);
        authUtil.authenticate(sessionId, userId, username, name);
    }
}
