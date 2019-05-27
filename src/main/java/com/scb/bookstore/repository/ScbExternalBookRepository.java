package com.scb.bookstore.repository;


import com.scb.bookstore.configuration.ScbBookUrlConfiguration;
import com.scb.bookstore.exception.ExternalRequestException;
import com.scb.bookstore.exception.UnexpectedException;
import com.scb.bookstore.model.book.Book;
import com.scb.bookstore.repository.impl.ScbExternalAllBooksImpl;
import com.scb.bookstore.repository.impl.ScbExternalRecommendedBooksImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScbExternalBookRepository {

    private ScbExternalAllBooksImpl scbAllBooks;
    private ScbExternalRecommendedBooksImpl scbRecommendBooks;
    private ScbBookUrlConfiguration scbBookUrlConfiguration;

    @Autowired
    public void setScbAllBooks(ScbExternalAllBooksImpl scbAllBooks) {
        this.scbAllBooks = scbAllBooks;
    }

    @Autowired
    public void setScbRecommendBooks(ScbExternalRecommendedBooksImpl scbRecommendBooks) {
        this.scbRecommendBooks = scbRecommendBooks;
    }

    @Autowired
    public void setScbBookUrlConfiguration(ScbBookUrlConfiguration scbBookUrlConfiguration) {
        this.scbBookUrlConfiguration = scbBookUrlConfiguration;
    }

    public List<Book> findAllBooking() {
        try {
            List<Book> allBooks = scbAllBooks.getBooks(scbBookUrlConfiguration.getAll());
            List<Integer> recommendedBooks = scbRecommendBooks.getBooks(scbBookUrlConfiguration.getRecommended());
            allBooks.stream()
                    .parallel()
                    .forEach(book -> {
                        if (recommendedBooks.contains(book.getId())) {
                            book.setIsRecommended(Boolean.TRUE);
                        }
                    });
            return allBooks;
        } catch (ExternalRequestException ex) {
           throw new ExternalRequestException("Can not get books from API.", ex.getDeveloperMessage());
        } catch (Exception ex) {
            throw new UnexpectedException(ex.getMessage());
        }
    }
}
