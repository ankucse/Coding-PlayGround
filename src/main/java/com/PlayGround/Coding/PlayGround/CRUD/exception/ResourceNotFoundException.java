package com.PlayGround.Coding.PlayGround.CRUD.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A custom exception thrown when a specific resource is requested but cannot be found in the system.
 * <p>
 * This exception is a {@link RuntimeException}, meaning it does not need to be explicitly caught.
 * The {@link ResponseStatus} annotation suggests that if this exception is uncaught by any other handler,
 * Spring should automatically respond with an HTTP 404 (Not Found) status.
 * In this application, it is explicitly handled by the {@link GlobalExceptionHandler} to provide a
 * more detailed, structured error response body.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}