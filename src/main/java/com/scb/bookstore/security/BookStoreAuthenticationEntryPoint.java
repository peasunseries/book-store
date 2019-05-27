package com.scb.bookstore.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.bookstore.configuration.JwtConfiguration;
import com.scb.bookstore.model.response.ApiErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

@Component
public class BookStoreAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private JwtConfiguration jwtConfiguration;

    @Autowired
    public void setJwtConfiguration(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String errorMessage = response.getHeader(jwtConfiguration.getError());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, errorMessage, authException.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        writer.println(mapper.writeValueAsString(errorResponse));
    }
}