package com.keepflower.api.exception;

import com.keepflower.api.common.error_code.ErrorCode;
import com.keepflower.api.common.response.ErrorResponseBody;
import com.keepflower.api.common.response.ResponseBody;
import com.keepflower.api.common.util.CookieUtil;
import com.keepflower.api.common.util.ErrorMessageUtil;
import com.keepflower.api.service.SessionService;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final CookieUtil cookieUtil;
    private final ErrorMessageUtil errorMessageUtil;
    private final MessageSource messageSource;
    private final SessionService sessionService;
    private final boolean isProd;

    public GlobalExceptionHandler(
            CookieUtil cookieUtil,
            ErrorMessageUtil errorMessageUtil,
            MessageSource messageSource,
            SessionService sessionService,
            Environment env) {
        this.cookieUtil = cookieUtil;
        this.errorMessageUtil = errorMessageUtil;
        this.messageSource = messageSource;
        this.sessionService = sessionService;
        isProd = Arrays.asList(env.getActiveProfiles()).contains("prod");
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
        var errorCode = ErrorCode.E002;
        String message = errorCode.getMessage(messageSource);
        var errors = errorMessageUtil.formatValidationErrors(e.getBindingResult());

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseBody(message, errorCode, errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBody> handleException(Exception e) {
        var errorCode = ErrorCode.E000;
        String message = isProd ? errorCode.getMessage(messageSource) : e.getMessage();

        return ResponseEntity
                .internalServerError()
                .body(new ErrorResponseBody(message, errorCode));
    }
}
