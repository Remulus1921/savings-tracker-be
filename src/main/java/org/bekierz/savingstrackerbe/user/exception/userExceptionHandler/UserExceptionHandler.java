package org.bekierz.savingstrackerbe.user.exception.userExceptionHandler;

import org.apache.tomcat.websocket.AuthenticationException;
import org.bekierz.savingstrackerbe.utils.exception.BaseExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class UserExceptionHandler extends BaseExceptionHandler {
    @ExceptionHandler(value = {
            BadCredentialsException.class,
            DisabledException.class
    })
    @Override
    public ResponseEntity<Object> handleForbiddenException(final RuntimeException ex, final WebRequest request) {
        return super.handleForbiddenException(ex, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Override
    public ResponseEntity<Object> handleValidationExceptions(final MethodArgumentNotValidException ex, final WebRequest request) {
        return super.handleValidationExceptions(ex, request);
    }
}
