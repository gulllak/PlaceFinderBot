package ru.gulllak.placefinder.exception;

public class NoHandlerFoundException extends RuntimeException {
    public NoHandlerFoundException(String message) {
        super(message);
    }

    public NoHandlerFoundException() {
    }
}
