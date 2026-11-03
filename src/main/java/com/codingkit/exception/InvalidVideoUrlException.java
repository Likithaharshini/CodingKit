package com.codingkit.exception;

public class InvalidVideoUrlException extends RuntimeException {
    public InvalidVideoUrlException(String message) {
        super(message);
    }
}
