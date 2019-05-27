package com.scb.bookstore.repository.inf;


import java.util.List;

public interface ExternalBookService<T> {

    List<T> getBooks(String url);
}
