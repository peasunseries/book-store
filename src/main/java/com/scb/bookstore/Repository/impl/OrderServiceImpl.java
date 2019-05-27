package com.scb.bookstore.Repository.impl;


import com.scb.bookstore.Repository.inf.OrderRepository;
import com.scb.bookstore.Repository.inf.OrderService;
import com.scb.bookstore.model.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service(value = "orderService")
@Transactional
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Order save(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public void deleteById(int id) {
		orderRepository.deleteById(id);
	}

	@Override
	public void deleteByUserId(int id) {
		orderRepository.deleteByUserId(id);
	}

	@Override
	public Order findById(int id) {
		Optional<Order> orderOptional =  orderRepository.findById(id);
		return orderOptional.isPresent() ? orderOptional.get() : null;
	}

	@Override
	public Order update(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public List<Order> findByUserId(int id) {
        return orderRepository.findByUserId( id);
	}
}
