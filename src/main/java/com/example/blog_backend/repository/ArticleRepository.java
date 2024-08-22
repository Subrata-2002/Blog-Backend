package com.example.blog_backend.repository;

import com.example.blog_backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>{

}
