package com.scb.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.bookstore.model.authentication.AuthenticationRequest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private AuthenticationRequest authenticationRequest  = new AuthenticationRequest();

    @Test
    public void step1_welcome_API_should_be_work_fine() throws Exception {
        mockMvc.perform(get("/welcome")
                .contentType("pain/text"))
                .andExpect(status().isOk());
    }

    @Test
    public void step2_loginTest() throws Exception {

        authenticationRequest.setUsername("chiwa");
        authenticationRequest.setPassword("123");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(content().string(containsString("Authentication failed, please check your username and password")))
                .andExpect(status().isUnauthorized());

        authenticationRequest.setUsername("chiwa");
        authenticationRequest.setPassword("password");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")));
    }

}