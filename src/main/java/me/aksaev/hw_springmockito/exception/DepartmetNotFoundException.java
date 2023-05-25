package me.aksaev.hw_springmockito.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404
public class DepartmetNotFoundException extends RuntimeException {
    public DepartmetNotFoundException() {
        super();
    }

    public DepartmetNotFoundException(String message) {
        super(message);
    }
}
