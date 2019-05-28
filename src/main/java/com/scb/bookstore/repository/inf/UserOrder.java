package com.scb.bookstore.repository.inf;

import com.scb.bookstore.model.book.Book;
import com.scb.bookstore.model.request.OrderRequest;
import com.scb.bookstore.model.response.OrderResponse;
import com.scb.bookstore.model.user.User;

import java.util.List;

public interface UserOrder {
     OrderResponse createNewOrderByUser(List<Book> bookList, User user, OrderRequest orderRequest);
     void deleteUserAndOrder(User user);
}
