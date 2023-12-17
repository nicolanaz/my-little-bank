package com.example.mylittlebank.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>(userNotFoundException.getMessage(), userNotFoundException.getHttpStatus());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleException(AccountNotFoundException accountNotFoundException) {
        return new ResponseEntity<>(accountNotFoundException.getMessage(), accountNotFoundException.getHttpStatus());
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<String> handleException(NotEnoughMoneyException notEnoughMoneyException) {
        return new ResponseEntity<>(notEnoughMoneyException.getMessage(), notEnoughMoneyException.getHttpStatus());
    }
}
