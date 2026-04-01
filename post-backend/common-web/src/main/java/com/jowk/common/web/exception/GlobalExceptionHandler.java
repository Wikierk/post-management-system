package com.jowk.common.web.exception;

import com.jowk.common.api.response.ErrorCode;
import com.jowk.common.api.response.ErrorResponse;
import com.jowk.common.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            EntityNotFoundException ex) {
        return createResponse(
                HttpStatus.NOT_FOUND,
                ErrorCode.RESOURCE_NOT_FOUND,
                ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArgument(
            IllegalArgumentException ex) {
        return createResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_ARGUMENT,
                ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(
            IllegalStateException ex) {
        return createResponse(
                HttpStatus.CONFLICT,
                ErrorCode.INVALID_STATE,
                ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> createResponse(
            HttpStatus status, ErrorCode code, String message) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                code,
                message,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(error);
    }

}