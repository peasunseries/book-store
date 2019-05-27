package com.scb.bookstore.Repository.inf;



import com.scb.bookstore.model.user.User;

import java.util.List;

public interface UserService {

    User findByUserName(String username);
    public User save(User user);
    User findById(int id);

}
