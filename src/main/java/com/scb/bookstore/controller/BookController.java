package com.scb.bookstore.controller;

import com.scb.bookstore.configuration.JwtConfiguration;
import com.scb.bookstore.exception.AuthenticationException;
import com.scb.bookstore.exception.DataNotFoundException;
import com.scb.bookstore.exception.ExternalRequestException;
import com.scb.bookstore.exception.UnexpectedException;
import com.scb.bookstore.model.book.Book;
import com.scb.bookstore.model.order.Order;
import com.scb.bookstore.model.request.OrderRequest;
import com.scb.bookstore.model.response.LoginResponse;
import com.scb.bookstore.model.response.OrderResponse;
import com.scb.bookstore.model.user.User;
import com.scb.bookstore.repository.ScbExternalBookRepository;
import com.scb.bookstore.repository.impl.OrderServiceImpl;
import com.scb.bookstore.repository.impl.UserOrderServiceImpl;
import com.scb.bookstore.repository.impl.UserServiceImpl;
import com.scb.bookstore.security.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@Api(value="Book", description="Api for manager books.")
public class BookController {

    private ScbExternalBookRepository scbExternalBookRepository;
    private JwtTokenService jwtTokenService;
    private UserServiceImpl userService;
    private OrderServiceImpl orderService;
    private UserOrderServiceImpl userOrderService;

    @Autowired
    public void setJwtTokenService(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setScbExternalBookRepository(ScbExternalBookRepository scbExternalBookRepository) {
        this.scbExternalBookRepository = scbExternalBookRepository;
    }

    @Autowired
    public void setUserOrderService(UserOrderServiceImpl userOrderService) {
        this.userOrderService = userOrderService;
    }

    @ApiOperation(value = "Get all books.", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of Books"),
            @ApiResponse(code = 401, message = "Authentication failed."),
            @ApiResponse(code = 500, message = "Unexpected exception.")
    })
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

    @ApiOperation(value = "Order books.", response = OrderResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 204, message = "Data not found."),
            @ApiResponse(code = 401, message = "Authentication failed."),
            @ApiResponse(code = 500, message = "Unexpected exception.")
    })
    @PostMapping("/users/orders")
    public OrderResponse orderBooks(HttpServletRequest req, @RequestBody OrderRequest orderRequest){

        try {
            final User user = jwtTokenService.getUserInformation(req);
            if (user == null) {
                log.error("User not found");
                throw new DataNotFoundException("User not found.", null);
            }
            if (orderRequest.getOrders().isEmpty()) {
                return new OrderResponse();
            } else {
                final List<Book> bookList = scbExternalBookRepository.findAllBooking();
                return userOrderService.createNewOrderByUser(bookList, user, orderRequest);
            }
        } catch (ExpiredJwtException ex) {
            log.error(ex.getMessage());
            throw new AuthenticationException("Token expired.",
                    ex.getMessage());
        }  catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UnexpectedException(ex.getMessage());
        }
    }
}
