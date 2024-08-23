package com.example.blog_backend.services;

import com.example.blog_backend.controller.MasterResponseBody;
import com.example.blog_backend.dto.ArticleDto;
import com.example.blog_backend.entity.Admin;
import com.example.blog_backend.entity.Article;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    MasterResponseBody<Admin> createUser(Admin admin);
    MasterResponseBody<String> loginUser(String Logindata, String pwd);
    MasterResponseBody<String> createArticle(Article article, MultipartFile heroImage);
    List<ArticleDto> getAllPublicArticles();
//    List<Article> getAllPrivateArticles();

}
