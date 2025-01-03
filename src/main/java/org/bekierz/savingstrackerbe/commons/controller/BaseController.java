package org.bekierz.savingstrackerbe.commons.controller;

import org.bekierz.savingstrackerbe.commons.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return wrapResponse(data, message, HttpStatus.OK);
    }

    protected ResponseEntity<ApiResponse<Void>> ok(String message) {
        return wrapResponse(null, message, HttpStatus.OK);
    }

    private <T> ResponseEntity<ApiResponse<T>> wrapResponse(T data, String message, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(data, message, status);
        return new ResponseEntity<>(response, status);
    }
}
