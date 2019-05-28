package com.scb.bookstore.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scb.bookstore.model.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Column
    @JsonProperty("order_id")
    private String orderId;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("date_of_order")
    private Date dateOfOrder;

    @Column
    @JsonProperty("user_id")
    private int userId;

    @Column
    @JsonProperty("book_id")
    private int bookId;
}
