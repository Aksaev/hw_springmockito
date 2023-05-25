package me.aksaev.hw_springmockito.exception;

public class IncorrectSurnameException extends RuntimeException {
    public IncorrectSurnameException() {
        super();
    }

    public IncorrectSurnameException(String message) {
        super(message);
    }
}
