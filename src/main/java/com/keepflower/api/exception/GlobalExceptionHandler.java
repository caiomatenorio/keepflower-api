package com.keepflower.api.exception;

import com.keepflower.api.common.error_code.ErrorCode;
import com.keepflower.api.common.response.ErrorResponseBody;
import com.keepflower.api.common.response.ResponseBody;
import com.keepflower.api.common.util.CookieUtil;
import com.keepflower.api.common.util.ErrorMessageUtil;
import com.keepflower.api.service.SessionService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final CookieUtil cookieUtil;
    private final ErrorMessageUtil errorMessageUtil;
    private final MessageSource messageSource;
    private final SessionService sessionService;
    private final Environment environment;

    private boolean isProd = true;

    @PostConstruct
    public void init() {
        isProd = Arrays.asList(environment.getActiveProfiles()).contains("prod");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseBody> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .headers(cookieUtil.cookiesToHeadersConsumer(sessionService.deleteSessionCookies()))
                .body(new ErrorResponseBody(e.getErrorMessage(messageSource), e.getErrorCode()));
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ResponseBody> handleHttpException(HttpException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new ErrorResponseBody(e.getErrorMessage(messageSource), e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.E002;
        String message = errorCode.getMessage(messageSource);
        Map<String, List<Map<String, String>>> errors = errorMessageUtil.formatValidationErrors(e.getBindingResult());

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseBody(message, errorCode, errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBody> handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.E000;
        String message = isProd ? errorCode.getMessage(messageSource) : e.getMessage();

        return ResponseEntity
                .internalServerError()
                .body(new ErrorResponseBody(message, errorCode));
    }
}
