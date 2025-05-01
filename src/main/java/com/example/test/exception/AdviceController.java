package com.example.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorMessageResponse messageResponse = new ErrorMessageResponse(
                "User Not Found", e.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessageResponse> handleUserNotFoundException(AccessDeniedException e) {
        ErrorMessageResponse messageResponse = new ErrorMessageResponse(
                "Access denied exception", e.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(messageResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessageResponse> handleUserNotFoundException(IllegalStateException e) {
        ErrorMessageResponse messageResponse = new ErrorMessageResponse(
                "Illegal state exception", e.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(messageResponse, HttpStatus.FORBIDDEN);
    }

}
