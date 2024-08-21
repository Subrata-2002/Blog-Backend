package com.example.blog_backend.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class LoginResponse extends MasterResponseBody<String>  {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    public LoginResponse(String userNotFound, int i) {
        super(userNotFound, i);
        this.token= null;
    }
    public LoginResponse(String token, String message, int status) {
        super(message, status);
        this.token = token;
    }
}
