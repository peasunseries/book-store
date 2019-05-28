package com.scb.bookstore.repository.inf;

import com.scb.bookstore.model.request.OrderRequest;
import com.scb.bookstore.model.response.OrderResponse;
import com.scb.bookstore.model.user.User;

public interface UserOrder {
     OrderResponse createNewOrderByUser(User user, OrderRequest orderRequest);
}
