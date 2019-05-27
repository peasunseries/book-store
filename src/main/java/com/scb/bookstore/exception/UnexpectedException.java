package com.scb.bookstore.exception;

public class UnexpectedException extends RuntimeException {

    public UnexpectedException(String errorMessage) {
        super(errorMessage);
    }
}
