package com.keepflower.api.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepflower.api.common.error_code.ErrorCode;
import com.keepflower.api.common.response.ErrorResponseBody;
import com.keepflower.api.common.response.ResponseBody;
import com.keepflower.api.common.util.ErrorMessageUtil;
import com.keepflower.api.common.util.JwtExtractUtil;
import com.keepflower.api.common.util.JwtUtil;
import com.keepflower.api.exception.HttpException;
import com.keepflower.api.exception.UnauthorizedException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final ErrorMessageUtil errorMessageUtil;
    private final JwtExtractUtil jwtExtractUtil;
    private final AuthUtil authUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtExtractUtil.extractFromServlet(request);
            
            if (!jwtUtil.isValid(jwt)) {
                throw new UnauthorizedException();
            }
            
            authUtil.authenticate(jwtUtil.getSessionId(jwt), jwtUtil.getUserId(jwt), jwtUtil.getUsername(jwt));
            filterChain.doFilter(request, response);
        } catch (HttpException e) {
        	ErrorCode errorCode = e.getErrorCode();
        	String message = errorMessageUtil.getCodeMessage(errorCode);
        	ErrorResponseBody responseBody = new ErrorResponseBody(message, errorCode);
        	writeResponse(response, errorCode.getStatusCode(), responseBody);
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return authUtil.getPublicPaths().contains(request.getRequestURI());
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
}
