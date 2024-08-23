package com.example.blog_backend.controller;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.blog_backend.dto.ArticleDto;
import com.example.blog_backend.dto.LoginDto;
import com.example.blog_backend.entity.Admin;
import com.example.blog_backend.entity.Article;
import com.example.blog_backend.services.AdminService;
import com.example.blog_backend.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<MasterResponseBody<Admin>> createUser(@RequestBody Admin admin){
        // Validate user data before saving
        MasterResponseBody<Admin> savedUser = adminService.createUser(admin);
        HttpStatus status;
        switch (savedUser.getStatus()) {
            case 500:
                status = HttpStatus.NOT_IMPLEMENTED;
                break;
            default:
                status = HttpStatus.OK;
        }
        return new ResponseEntity<>(savedUser, status);
    }


    @PostMapping("/login")
    public ResponseEntity<MasterResponseBody<String>> loginUser(@RequestBody LoginDto logindto){
//        String token = String.valueOf(userService.loginUser(logindto.getLogindata(), logindto.getPwd()));
        System.out.println("welcome to login");
        MasterResponseBody<String> bb= adminService.loginUser(logindto.getLogindata(), logindto.getPwd());
        return new ResponseEntity<>(bb, HttpStatusCode.valueOf(bb.getStatus()));
    };


    @PostMapping("/create")
    public ResponseEntity<MasterResponseBody<String>> createArticle(
            @RequestParam("title") String title,
            @RequestParam("htmlContent") String htmlContent,
            @RequestParam("isPublic") boolean isPublic,
            @RequestParam("heroImage") MultipartFile heroImage) {

            // Create the Article object
            Article article = new Article();
            article.setTitle(title);
            article.setHtmlContent(htmlContent);
            article.setPublic(isPublic);

            // Call the service to save the article
            MasterResponseBody<String> savedArticle = adminService.createArticle(article, heroImage);

            // Determine the response status based on the service response
            HttpStatus status;
            switch (savedArticle.getStatus()) {
                case 500:
                    status = HttpStatus.NOT_IMPLEMENTED;
                    break;
                default:
                    status = HttpStatus.OK;
            }
            // Return the response
            return new ResponseEntity<>(savedArticle, status);

    }


    @GetMapping("/public")
    public ResponseEntity<List<ArticleDto>> getAllPublicArticles() {
        List<ArticleDto> articles = adminService.getAllPublicArticles();
        return ResponseEntity.ok(articles);
    }
}
