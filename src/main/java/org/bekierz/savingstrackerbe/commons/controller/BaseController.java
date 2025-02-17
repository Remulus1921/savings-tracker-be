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

    protected <T> ResponseEntity<ApiResponse<Void>> created(String message) {
        return wrapResponse(null, message, HttpStatus.CREATED);
    }

    protected <T> ResponseEntity<ApiResponse<Void>> noContent(String message) {
        return wrapResponse(null, message, HttpStatus.NO_CONTENT);
    }

    private <T> ResponseEntity<ApiResponse<T>> wrapResponse(T data, String message, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(data, message, status);
        return new ResponseEntity<>(response, status);
    }
}
