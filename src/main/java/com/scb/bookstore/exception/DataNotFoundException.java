package com.scb.bookstore.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException {

    private String developerMessage;

    public DataNotFoundException(String errorMessage, String developerMessage) {
        super(errorMessage);
        this.developerMessage = developerMessage;
    }
}
