package com.scb.bookstore.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderRequest {

    @JsonProperty("orders")
    List<Integer> orders = new ArrayList<>();
}
