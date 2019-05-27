package com.scb.bookstore.Repository.inf;


import com.scb.bookstore.model.order.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    List<Order> findByUserId(int id);
    void deleteByUserId(int id);
}
