package com.scb.bookstore.exception;

import lombok.Getter;

@Getter
public class DatabaseException extends RuntimeException {

    private String developerMessage;

    public DatabaseException(String errorMessage, String developerMessage) {
        super(errorMessage);
        this.developerMessage = developerMessage;
    }
}
