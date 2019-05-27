package com.scb.bookstore.controller;

import com.scb.bookstore.exception.AuthenticationException;
import com.scb.bookstore.exception.ExternalRequestException;
import com.scb.bookstore.exception.UnexpectedException;
import com.scb.bookstore.model.book.Book;
import com.scb.bookstore.repository.ScbExternalBookRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@Api(value="Book", description="Api for manager books.")
public class BookController {

    private ScbExternalBookRepository scbExternalBookRepository;

    @Autowired
    public void setScbExternalBookRepository(ScbExternalBookRepository scbExternalBookRepository) {
        this.scbExternalBookRepository = scbExternalBookRepository;
    }

    @GetMapping(value = "/books")
    public List<Book> findAllBooks(HttpServletRequest req){
        try {
            return scbExternalBookRepository.findAllBooking();
        } catch (ExpiredJwtException ex) {
            log.error(ex.getMessage());
            throw new AuthenticationException("Token expired.", ex.getMessage());
        }  catch (ExternalRequestException ex) {
                log.error(ex.getMessage());
                throw new ExternalRequestException(ex.getMessage(), ex.getDeveloperMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UnexpectedException(ex.getMessage());
        }
    }

}
