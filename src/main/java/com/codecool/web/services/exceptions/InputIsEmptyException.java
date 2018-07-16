package com.codecool.web.services.exceptions;

public class InputIsEmptyException extends Exception {
    public InputIsEmptyException() {
    }

    public InputIsEmptyException(String message) {
        super(message);
    }
}
