package com.scb.bookstore.exception;

import lombok.Getter;

@Getter
public class ExternalRequestException extends RuntimeException {

    private String developerMessage;

    public ExternalRequestException(String errorMessage, String developerMessage) {
        super(errorMessage);
        this.developerMessage = developerMessage;
    }
}
