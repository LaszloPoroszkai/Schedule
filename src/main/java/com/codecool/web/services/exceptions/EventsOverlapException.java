package com.codecool.web.services.exceptions;

public class EventsOverlapException extends Exception {
    public EventsOverlapException() {
    }

    public EventsOverlapException(String message) {
        super(message);
    }
}