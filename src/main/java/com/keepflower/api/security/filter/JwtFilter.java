package com.keepflower.api.security.filter;

import com.keepflower.api.common.exception.UnauthorizedException;
import com.keepflower.api.common.util.AuthUtil;
import com.keepflower.api.common.util.JwtUtil;
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
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AuthUtil authUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException, UnauthorizedException {
        final String jwt = getJwtFromRequest(request);

        if (jwtUtil.isValid(jwt)) {
            UUID sessionId = jwtUtil.getSessionId(jwt);
            UUID userId = jwtUtil.getUserId(jwt);
            String username = jwtUtil.getUsername(jwt);
            String name = jwtUtil.getName(jwt);
            authUtil.authenticate(sessionId, userId, username, name);
            filterChain.doFilter(request, response);
            return;
        }

        throw new UnauthorizedException();
    }

    private @Nullable String getJwtFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> "access_token".equals(cookie.getName()))
                        .findFirst())
                .map(Cookie::getValue)
                .orElse(null);
    }
}
