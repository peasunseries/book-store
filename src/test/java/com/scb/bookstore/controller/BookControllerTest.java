package com.scb.bookstore.controller;

import com.scb.bookstore.exception.ExternalRequestException;
import com.scb.bookstore.model.book.Book;
import com.scb.bookstore.model.user.User;
import com.scb.bookstore.repository.ScbExternalBookRepository;
import com.scb.bookstore.repository.impl.UserOrderServiceImpl;
import com.scb.bookstore.repository.impl.UserServiceImpl;
import com.scb.bookstore.security.JwtTokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserServiceImpl userService;

    @Mock
    private ScbExternalBookRepository scbExternalBookRepository;

    @Mock
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserOrderServiceImpl userOrderService;

    @InjectMocks
    private BookController bookController;

    @Before
    public void setScbExternalBookRepository() throws Exception {
        bookController.setUserOrderService(userOrderService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void findAllBooks() throws Exception {
        List<Book> bookList = new ArrayList<>();
        Book book = new Book(1, "JAVA", "Chiwa Kantawong", 750.0);
        bookList.add(book);
        Mockito.when(scbExternalBookRepository.findAllBooking()).thenReturn(bookList);
        mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Chiwa Kantawong")))
                .andExpect(status().isOk());
    }

    //TODO: rewrite this test if have time
    @Test(expected = NestedServletException.class)
    public void expectExternalRequestException() throws Exception {
        Mockito.when(scbExternalBookRepository.findAllBooking()).thenThrow(new ExternalRequestException("Error", "Error"));
        mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void userOrder() throws Exception {
        List<Book> bookList = new ArrayList<>();
        Book book = new Book(5, "JAVA", "Chiwa Kantawong", 750.0);
        bookList.add(book);
        book = new Book(6, "PHP", "Nirucha Kantawong", 550.0);
        bookList.add(book);
        Mockito.when(scbExternalBookRepository.findAllBooking()).thenReturn(bookList);
        User user = userService.findById(1);
        //user.setOrders(new ArrayList<>());
        Mockito.when(jwtTokenService.getUserInformation(any())).thenReturn(user);
        mockMvc.perform(post("/users/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"orders\": [5, 6] }"))
                .andExpect(content().string(containsString("{\"price\":1300.0}")))
                .andExpect(status().isOk());
    }


    @Test(expected = NestedServletException.class)
    public void userOrderFail() throws Exception {
        List<Book> bookList = new ArrayList<>();
        Book book = new Book(5, "JAVA", "Chiwa Kantawong", 750.0);
        bookList.add(book);
        book = new Book(6, "PHP", "Nirucha Kantawong", 550.0);
        bookList.add(book);
        Mockito.when(scbExternalBookRepository.findAllBooking()).thenReturn(bookList);
        User user = userService.findById(1);
        user.setOrders(new ArrayList<>());
        Mockito.when(jwtTokenService.getUserInformation(any())).thenReturn(user);
        mockMvc.perform(post("/users/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"orders\": [1, 2] }"))
                .andExpect(content().string(containsString("{\"price\":1078.7}")))
                .andExpect(status().isOk());
    }
}