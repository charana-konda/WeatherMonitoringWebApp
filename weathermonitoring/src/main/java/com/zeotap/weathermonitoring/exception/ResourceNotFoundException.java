package com.zeotap.weathermonitoring.exception;



/**
 * Custom exception class to handle resource not found errors.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}