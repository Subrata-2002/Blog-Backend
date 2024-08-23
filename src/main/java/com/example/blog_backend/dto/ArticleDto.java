package com.example.blog_backend.dto;

public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private String image;
    private String access_Type;


    @Override
    public String toString() {
        return "ArticleDto{" +
                "id -" + id +
                ", title- '" + title + '\'' +
                ", content- '" + content + '\'' +
                ", image -'" + image + '\'' +
                ", access_Type -'" + access_Type + '\'' +
                '}';
    }
}
