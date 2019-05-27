package com.scb.bookstore.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@Api(value="User", description="Api for manager users.")
public class UserController {

    @ApiOperation(value = "Welcome API.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Welcome user!")
    })
    @GetMapping(value = "/welcome")
    public String hello(HttpServletRequest req){
        return "Welcome user!";
    }

}
