package com.scb.bookstore.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderResponse {

    @JsonProperty("price")
    private Double price;
}
