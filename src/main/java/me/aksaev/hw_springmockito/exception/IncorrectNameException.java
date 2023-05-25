package me.aksaev.hw_springmockito.exception;

public class IncorrectNameException extends RuntimeException {
    public IncorrectNameException() {
        super();
    }

    public IncorrectNameException(String message) {
        super(message);
    }
}
