package com.scb.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.bookstore.model.authentication.AuthenticationRequest;
import com.scb.bookstore.model.response.LoginResponse;
import org.junit.Assert;
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
import org.springframework.test.web.servlet.MvcResult;

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

    @Test
    public void step3_getLoggedUserInformationTest() throws Exception {
        authenticationRequest.setUsername("chiwa");
        authenticationRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")))
                .andReturn();
        String content = result.getResponse().getContentAsString();

        LoginResponse loginResponse = objectMapper.readValue(content, LoginResponse.class);

        String header = "SCB " + loginResponse.getToken();
        result = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", header))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void step4_registerUserTest() throws Exception {
        authenticationRequest.setUsername("chiwa");
        authenticationRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")))
                .andReturn();
        String content = result.getResponse().getContentAsString();

        LoginResponse loginResponse = objectMapper.readValue(content, LoginResponse.class);

        String header = "SCB " + loginResponse.getToken();
        result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"username\":\"test\",\n" +
                        "\t\"password\":\"test\",\n" +
                        "\t\"first_name\" : \"test\",\n" +
                        "\t\"last_name\" : \"test\",\n" +
                        "\t\"date_of_birth\" : \"10/01/1977\"\n" +
                        "}")
                .header("authorization", header))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertTrue(result.getResponse().getContentAsString().equalsIgnoreCase("{\"username\":\"test\",\"first_name\":\"test\",\"last_name\":\"test\",\"date_of_birth\":\"10/01/1977\",\"books\":[]}"));

        result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"password\":\"test\",\n" +
                        "\t\"first_name\" : \"test\",\n" +
                        "\t\"last_name\" : \"test\",\n" +
                        "\t\"date_of_birth\" : \"10/01/1977\"\n" +
                        "}")
                .header("authorization", header))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}