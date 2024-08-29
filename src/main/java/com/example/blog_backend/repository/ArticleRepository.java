package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Admin;
import com.example.blog_backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>{
    List<Article> findByIsPublicTrue();
    List<Article> findByIsPublicFalse();
    Optional<Article> findById(Long id);

}
