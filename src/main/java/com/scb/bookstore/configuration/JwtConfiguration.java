package com.scb.bookstore.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt.token")
@Getter
@Setter
@ToString
public class JwtConfiguration {

    private long expired;
    private String signingKey;
    private String prefix;
    private String header;
    private String error;
}
