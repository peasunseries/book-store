package com.scb.bookstore.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("scb.uri.book")
@Getter
@Setter
@ToString
public class ScbBookUrlConfiguration {

    private String all;
    private String recommended;
}
