package com.keepflower.api.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepflower.api.common.error_code.ErrorCode;
import com.keepflower.api.common.response.ErrorResponseBody;
import com.keepflower.api.common.response.ResponseBody;
import com.keepflower.api.common.util.AuthUtil;
import com.keepflower.api.common.util.ErrorMessageUtil;
import com.keepflower.api.common.util.JwtUtil;
import com.keepflower.api.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AuthUtil authUtil;
    private final ObjectMapper objectMapper;
    private final ErrorMessageUtil errorMessageUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException, UnauthorizedException {
        String jwt = getJwtFromRequest(request);

        if (!jwtUtil.isValid(jwt)) {
            ErrorCode errorCode = ErrorCode.E001;
            String message = errorMessageUtil.getCodeMessage(errorCode);
            ErrorResponseBody responseBody = new ErrorResponseBody(message, errorCode);
            writeResponse(response, errorCode.getStatusCode(), responseBody);
            return;
        }

        authenticate(jwt);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return Set.of("/ping", "/signup", "/login", "/auth/refresh", "/auth/status").contains(request.getRequestURI());
    }

    private @Nullable String getJwtFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals("access_token"))
                        .findFirst())
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void writeResponse(
            HttpServletResponse response,
            int statusCode,
            ResponseBody responseBody) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), responseBody);
    }

    private void authenticate(String jwt) {
        UUID sessionId = jwtUtil.getSessionId(jwt);
        UUID userId = jwtUtil.getUserId(jwt);
        String username = jwtUtil.getUsername(jwt);
        String name = jwtUtil.getName(jwt);
        authUtil.authenticate(sessionId, userId, username, name);
    }
}
