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

    /**
     * Handles {@link UnauthorizedException} by clearing the session cookies and
     * returning an error response with the status code set to the value of the
     * exception and the error code and message from the exception.
     *
     * @param e the exception to handle
     * @return a response entity that will be returned to the client
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseBody> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .headers(cookieUtil.cookiesToHeadersConsumer(sessionService.deleteSessionCookies()))
                .body(new ErrorResponseBody(e.getErrorMessage(messageSource), e.getErrorCode()));
    }

    /**
     * Handles {@link HttpException} by returning a response entity with the
     * status code set to the value of the exception and the error code and
     * message from the exception.
     *
     * @param e the exception to handle
     * @return a response entity that will be returned to the client
     */
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ResponseBody> handleHttpException(HttpException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new ErrorResponseBody(e.getErrorMessage(messageSource), e.getErrorCode()));
    }

    /**
     * Handles {@link MethodArgumentNotValidException} by returning a response
     * entity with the status code set to 400 and the error code and message from
     * the exception. The response body will contain the validation errors.
     *
     * @param e the exception to handle
     * @return a response entity that will be returned to the client
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.E002;
        String message = errorCode.getMessage(messageSource);
        Map<String, List<Map<String, String>>> errors = errorMessageUtil.formatValidationErrors(e.getBindingResult());

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseBody(message, errorCode, errors));
    }

    /**
     * Handles any exception by returning a response entity with the status code
     * set to 500 and the error code and message from the exception. The error
     * message will be localized if in a non-production environment, otherwise it
     * will be an internal server error message.
     *
     * @param e the exception to handle
     * @return a response entity that will be returned to the client
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBody> handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.E000;
        String message = isProd ? errorCode.getMessage(messageSource) : e.getMessage();

        return ResponseEntity
                .internalServerError()
                .body(new ErrorResponseBody(message, errorCode));
    }
}
