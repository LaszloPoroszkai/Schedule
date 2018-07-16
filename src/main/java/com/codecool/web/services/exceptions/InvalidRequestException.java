package com.codecool.web.services.exceptions;

public class InvalidRequestException extends Exception {
    public InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
