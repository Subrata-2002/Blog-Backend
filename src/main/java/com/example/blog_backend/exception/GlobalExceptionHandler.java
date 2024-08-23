//package com.example.blog_backend.exception;
//
//import com.auth0.jwt.exceptions.TokenExpiredException;
//import com.example.blog_backend.controller.MasterResponseBody;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler{
//    @ExceptionHandler(TokenExpiredException.class)
//    public ResponseEntity<Map<String, Object>> handleTokenExpiredException(TokenExpiredException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "Token expired!! Please login again");
//        response.put("status", "401");
//
//        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//    }
//}
