package com.medicalrecord.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // Message only
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Message + cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Cause only
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    // Default constructor
    public ResourceNotFoundException() {
        super("Resource not found");
    }
}
