package com.scb.bookstore.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiErrorResponse {

    @JsonProperty("status_code")
    private int statusCode;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("developer_message")
    private String developerMessage;

    public ApiErrorResponse(int statusCode, String errorMessage, String developerMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.developerMessage = developerMessage;
    }

}
