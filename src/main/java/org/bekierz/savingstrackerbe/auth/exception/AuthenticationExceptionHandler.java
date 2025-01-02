package org.bekierz.savingstrackerbe.auth.exception;

import org.bekierz.savingstrackerbe.utils.exception.BaseExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public class AuthenticationExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(value = {
            BadCredentialsException.class,
            DisabledException.class
    })
    @Override
    public ResponseEntity<Object> handleForbiddenException(final RuntimeException ex, final WebRequest request) {
        return super.handleForbiddenException(ex, request);
    }

    @ExceptionHandler(value = {
            AuthenticationNotFoundException.class
    })
    @Override
    public ResponseEntity<Object> handleNotFoundException(final RuntimeException ex, final WebRequest request) {
        return super.handleNotFoundException(ex, request);
    }
}
