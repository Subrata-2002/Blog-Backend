package com.example.blog_backend.controller;

import com.fasterxml.jackson.annotation.JsonInclude;

public class LoginResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    public LoginResponse(String userNotFound, int i) {
//        super(userNotFound, i);
        token= null;
    }
    public LoginResponse(String token, String message, int status) {
//        super(message, status);
        this.token = token;

    }
}
