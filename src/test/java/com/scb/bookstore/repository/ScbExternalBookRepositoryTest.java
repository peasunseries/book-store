package com.scb.bookstore.repository;



import com.scb.bookstore.configuration.ScbBookUrlConfiguration;
import com.scb.bookstore.model.book.Book;
import com.scb.bookstore.repository.impl.ScbExternalAllBooksImpl;
import com.scb.bookstore.repository.impl.ScbExternalRecommendedBooksImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


public class ScbExternalBookRepositoryTest {

    private ScbExternalBookRepository bookRepository = new ScbExternalBookRepository();
    private ScbExternalAllBooksImpl scbAllBooking = Mockito.mock(ScbExternalAllBooksImpl.class);
    private ScbExternalRecommendedBooksImpl scbRecommendBooking = Mockito.mock(ScbExternalRecommendedBooksImpl.class);

    @Before
    public void setUp() {
        bookRepository.setScbAllBooks(scbAllBooking);
        bookRepository.setScbRecommendBooks(scbRecommendBooking);
        bookRepository.setScbBookUrlConfiguration(new ScbBookUrlConfiguration());
    }

    @Test
    public void findAllBooking_empty_Test() throws Exception {
        when(scbAllBooking.getBooks(any())).thenReturn(new ArrayList<>());
        when(scbRecommendBooking.getBooks(any())).thenReturn(new ArrayList<>());
        List<Book> bookList = bookRepository.findAllBooking();
        Assert.assertTrue(bookList.isEmpty());
    }

    @Test
    public void findAllBooking_recommend_Test() throws Exception {
        List<Book> allBookings = new ArrayList<>();
        allBookings.add(new Book(1, "PHP", "Devid", 150.0));
        allBookings.add(new Book(2, "Java", "Joe", 250.0));
        allBookings.add(new Book(3, "C++", "Nida", 350.50));
        allBookings.add(new Book(4, "Python", "Pawanrat", 430.0));
        when(scbAllBooking.getBooks(any())).thenReturn(allBookings);
        when(scbRecommendBooking.getBooks(any())).thenReturn(new ArrayList<>());
        List<Book> bookList = bookRepository.findAllBooking();
        Assert.assertTrue(!bookList.isEmpty());
        Assert.assertTrue(bookList.size() == 4);
        Assert.assertTrue(0 == bookList.stream()
                .filter(book -> book.getIsRecommended().equals(Boolean.TRUE))
                .count());

        final List<Integer> recommendedBooks = Arrays.asList(1, 4);
        when(scbRecommendBooking.getBooks(any())).thenReturn(recommendedBooks);
        bookList = bookRepository.findAllBooking();
        Assert.assertTrue(2 == bookList.stream()
                .filter(book -> book.getIsRecommended().equals(Boolean.TRUE))
                .count());

        bookList.stream().forEach(book -> {
            if (recommendedBooks.contains(book.getId())) {
                Assert.assertTrue(book.getIsRecommended().equals(Boolean.TRUE));
            } else {
                Assert.assertTrue(book.getIsRecommended().equals(Boolean.FALSE));
            }
        });
    }

}