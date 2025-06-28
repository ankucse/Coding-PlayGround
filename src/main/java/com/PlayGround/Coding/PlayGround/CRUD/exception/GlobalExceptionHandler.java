package com.PlayGround.Coding.PlayGround.CRUD.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the REST API.
 * This class uses @ControllerAdvice to centralize exception handling logic across all @RestController classes.
 * It catches specific exceptions and formats the HTTP response accordingly, ensuring consistent error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Logger for this class to log exception details.
     */
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles {@link ResourceNotFoundException}.
     * This exception is thrown when a requested resource (e.g., a user by ID) is not found in the system.
     *
     * @param ex      The caught {@link ResourceNotFoundException}.
     * @param request The current web request, used to extract details like the request URI.
     * @return a {@link ResponseEntity} with a 404 Not Found status and a detailed error body.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles all other uncaught exceptions as a fallback.
     * This method ensures that any unexpected error will result in a generic,
     * user-friendly error message and a 500 Internal Server Error status.
     * It logs the full stack trace for debugging purposes.
     *
     * @param ex      The caught {@link Exception}.
     * @param request The current web request.
     * @return a {@link ResponseEntity} with a 500 Internal Server Error status and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "An unexpected internal server error occurred.");
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles {@link DataIntegrityViolationException}.
     * This typically occurs when a database constraint is violated, such as attempting to insert a user
     * with an email that already exists (violating a unique constraint).
     *
     * @param ex      The caught {@link DataIntegrityViolationException}.
     * @param request The current web request.
     * @return a {@link ResponseEntity} with a 409 Conflict status and a message indicating a constraint violation.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        log.warn("Data integrity violation: {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", "Database constraint violation. A resource with the provided email may already exist.");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Optionally include the root cause for better debugging
        // body.put("details", ex.getMostSpecificCause().getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

}