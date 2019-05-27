package com.scb.bookstore.repository.inf;



import com.scb.bookstore.model.order.Order;

import java.util.List;

public interface OrderService {

    Order save(Order order);
    void deleteById(int id);
    void deleteByUserId(int id);
    Order findById(int id);
    Order update(Order order);
    List<Order> findByUserId(int id);
}
