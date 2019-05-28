package com.scb.bookstore.repository.impl;

import com.scb.bookstore.exception.DataNotFoundException;
import com.scb.bookstore.model.book.Book;
import com.scb.bookstore.model.order.Order;
import com.scb.bookstore.model.request.OrderRequest;
import com.scb.bookstore.model.response.OrderResponse;
import com.scb.bookstore.model.user.User;
import com.scb.bookstore.repository.ScbExternalBookRepository;
import com.scb.bookstore.repository.inf.UserOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserOrderServiceImpl implements UserOrder {

    private ScbExternalBookRepository scbExternalBookRepository;
    private UserServiceImpl userService;
    private OrderServiceImpl orderService;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setScbExternalBookRepository(ScbExternalBookRepository scbExternalBookRepository) {
        this.scbExternalBookRepository = scbExternalBookRepository;
    }

    @Transactional
    public OrderResponse createNewOrderByUser(List<Book> bookList, User user, OrderRequest orderRequest) {
        OrderResponse orderResponse = new OrderResponse();
        List<Order> orders = new ArrayList<>();
        String orderId = UUID.randomUUID().toString();
        for (int bookId : orderRequest.getOrders()) {
            Book book = bookList.stream()
                    .filter(b -> b.getId() == bookId)
                    .findFirst().orElse(null);
            if (book == null) {
                final String message = bookId + " is not found.";
                throw new DataNotFoundException(message, message);
            }
            orderResponse.setPrice(orderResponse.getPrice() + book.getPrice());
            Order order = new Order();
            order.setOrderId(orderId);
            order.setDateOfOrder(new Date());
            order.setBookId(bookId);
            order.setUserId(user.getId());
            orders.add(order);
            orderService.save(order);
        }
        return orderResponse;
    }

    @Override
    public void deleteUserAndOrder(User user) {
        orderService.deleteByUserId(user.getId());
        userService.deleteById(user.getId());
    }
}
