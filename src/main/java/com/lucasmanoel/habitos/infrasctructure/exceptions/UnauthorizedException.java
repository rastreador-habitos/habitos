package com.lucasmanoel.habitos.infrasctructure.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message, Throwable throwable) {
        super(message);
    }
    public UnauthorizedException(String message) {
        super(message);
    }
}
