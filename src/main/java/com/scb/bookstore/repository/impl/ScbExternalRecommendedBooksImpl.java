package com.scb.bookstore.repository.impl;


import com.scb.bookstore.model.book.Book;
import com.scb.bookstore.repository.inf.ExternalBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service("scbExternalRecommendedBooksImpl")
public class ScbExternalRecommendedBooksImpl implements ExternalBookService {

    RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Integer> getBooks(String url) {
        ResponseEntity<List<Book>> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Book>>() {
                        });
        return responseEntity.getBody()
        .stream().parallel()
        .map(Book::getId)
        .collect(Collectors.toList());
    }
}
