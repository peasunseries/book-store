package com.scb.bookstore.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResponse {

    public LoginResponse() {
        super();
    }
    public void LoginResponse(String username, String token) {
        this.username = username;
        this.token = token;
    }

    @JsonProperty("user_name")
    private String username;

    @JsonProperty("token")
    private String token;

    public  LoginResponse(String username, String token) {
        this.username = username;
        this.token = token;

    }
}
