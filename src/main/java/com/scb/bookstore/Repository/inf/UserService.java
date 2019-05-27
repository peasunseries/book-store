package com.scb.bookstore.Repository.inf;



import com.scb.bookstore.model.user.User;

import java.util.List;

public interface UserService {

    User findByUserName(String username);
    User save(User user);
    void deleteById(int id);
    User findById(int id);

}
