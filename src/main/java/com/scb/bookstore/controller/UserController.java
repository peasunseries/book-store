package com.scb.bookstore.controller;

import com.scb.bookstore.exception.AuthenticationException;
import com.scb.bookstore.exception.DataNotFoundException;
import com.scb.bookstore.exception.DatabaseException;
import com.scb.bookstore.exception.UnexpectedException;
import com.scb.bookstore.model.request.AuthenticationRequest;
import com.scb.bookstore.model.response.LoginResponse;
import com.scb.bookstore.model.user.User;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@Api(value="User", description="Api for manager users.")
public class UserController {

    private AuthenticationManager authenticationManager;
    private JwtTokenService jwtTokenService;
    private UserServiceImpl userService;
    private UserOrderServiceImpl userOrderService;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtTokenService(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }


    @Autowired
    public void setUserOrderService(UserOrderServiceImpl userOrderService) {
        this.userOrderService = userOrderService;
    }

    @ApiOperation(value = "Welcome API.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Welcome user!")
    })
    @GetMapping(value = "/welcome")
    public String hello(HttpServletRequest req){
        return "Welcome user!";
    }

    @ApiOperation(value = "Login and get user informations and token.", response = LoginResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login Successful"),
            @ApiResponse(code = 401, message = "Authentication failed."),
            @ApiResponse(code = 500, message = "Unexpected exception.")
    })
    @PostMapping(value = "/login")
    public LoginResponse login(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {
        try {
            final User user = userService.findByUserName(authenticationRequest.getUsername());
            if (user == null) {
                log.error("User {} not found", authenticationRequest.getUsername());
                throw new AuthenticationException("User " + authenticationRequest.getUsername() + " not found.", null);
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));

            final String token = jwtTokenService.generateToken(user.getUsername());
            return new LoginResponse(authenticationRequest.getUsername(), token);
        } catch (BadCredentialsException ex) {
            log.error(ex.getMessage());
            throw new AuthenticationException("Authentication failed, please check your username and password",
                    ex.getMessage());
        }
    }

    @ApiOperation(value = "Get user informations from token (current login user).", response = LoginResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 204, message = "No content."),
            @ApiResponse(code = 401, message = "Authentication failed."),
            @ApiResponse(code = 500, message = "Unexpected exception.")
    })
    @GetMapping(value = "/users")
    public User getLoggedUserData(HttpServletRequest req){
        try {
            final User user = jwtTokenService.getUserInformation(req);
            if (user == null) {
                log.error("User not found");
                throw new DataNotFoundException("User not found.", null);
            }
            return user;
        } catch (ExpiredJwtException ex) {
            log.error(ex.getMessage());
            throw new AuthenticationException("Token expired.",
                    ex.getMessage());
        }
    }

    @ApiOperation(value = "Register new user.", response = LoginResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Bad request,  Invalid data [missing required data, duplicated data]."),
            @ApiResponse(code = 401, message = "Authentication failed."),
            @ApiResponse(code = 500, message = "Unexpected exception.")
    })
    @PostMapping("/users")
    public User registerUser(@RequestBody User newUser){
        try {
            return  userService.save(newUser);
        }catch (ExpiredJwtException ex) {
            log.error(ex.getMessage());
            throw new AuthenticationException("Token expired.",
                    ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            log.error(ex.getMessage());
            throw new DatabaseException("Invalid data [missing required data, duplicated data]", ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UnexpectedException(ex.getMessage());
        }
    }

    @ApiOperation(value = "Delete user and orders history.", response = LoginResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 401, message = "Authentication failed."),
            @ApiResponse(code = 500, message = "Unexpected exception.")
    })
    @DeleteMapping(value = "/users")
    public void deleteUserOrderDetail(HttpServletRequest req){
        try {
            final User user = jwtTokenService.getUserInformation(req);
            if (user == null) {
                log.error("User not found");
                throw new DataNotFoundException("User not found.", null);
            }
            userOrderService.deleteUserAndOrder(user);
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
