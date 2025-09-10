package com.nikhil.project.exception;

import com.nikhil.project.utility.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {

        Log.log("backend", "error", "handler", ex.getMessage());

        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ShortcodeNotFoundException.class)
    public ResponseEntity<Object> handleShortcodeNotFound(ShortcodeNotFoundException ex) {
        Log.log("backend", "error", "GlobalExceptionHandler", "Shortcode not found: " + ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ShortcodeCollisionException.class)
    public ResponseEntity<Object> handleShortcodeCollision(ShortcodeCollisionException ex) {
        Log.log("backend", "error", "GlobalExceptionHandler", "Shortcode collision: " + ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
            Log.log("backend", "error", "GlobalExceptionHandler", "Validation error on field " + error.getField() + ": " + error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
