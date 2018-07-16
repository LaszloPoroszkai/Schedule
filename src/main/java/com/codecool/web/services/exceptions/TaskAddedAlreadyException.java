package com.codecool.web.services.exceptions;

public class TaskAddedAlreadyException extends Exception {
    public TaskAddedAlreadyException() {
    }

    public TaskAddedAlreadyException(String message) {
        super(message);
    }
}
