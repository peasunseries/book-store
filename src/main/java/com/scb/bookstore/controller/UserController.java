package com.scb.bookstore.controller;

import com.scb.bookstore.Repository.impl.UserServiceImpl;
import com.scb.bookstore.configuration.JwtConfiguration;
import com.scb.bookstore.exception.AuthenticationException;
import com.scb.bookstore.exception.DataNotFoundException;
import com.scb.bookstore.exception.DatabaseException;
import com.scb.bookstore.exception.UnexpectedException;
import com.scb.bookstore.model.authentication.AuthenticationRequest;
import com.scb.bookstore.model.response.LoginResponse;
import com.scb.bookstore.model.user.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@Api(value="User", description="Api for manager users.")
public class UserController {

    private AuthenticationManager authenticationManager;
    private JwtTokenService jwtTokenService;
    private JwtConfiguration jwtConfiguration;
    private UserServiceImpl userService;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtTokenService(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Autowired
    public void setJwtConfiguration(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }


    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
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
            final String header = req.getHeader(jwtConfiguration.getHeader());
            final String token = header.replace(jwtConfiguration.getPrefix(), "");
            final String userName = jwtTokenService.getUsernameFromToken(token);
            final User user = userService.findByUserName(userName);
            if (user == null) {
                log.error("User {} not found", userName);
                throw new DataNotFoundException("User " + userName + " not found.", null);
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

}
