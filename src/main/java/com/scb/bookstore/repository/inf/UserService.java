package com.scb.bookstore.repository.inf;



import com.scb.bookstore.model.user.User;

public interface UserService {

    User findByUserName(String username);
    User save(User user);
    void deleteById(int id);
    User findById(int id);

}
