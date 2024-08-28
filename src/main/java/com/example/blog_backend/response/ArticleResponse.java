package com.example.blog_backend.response;

import com.example.blog_backend.controller.MasterResponseBody;
import com.example.blog_backend.entity.Article;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse extends MasterResponseBody<String> {

//    private String message;
//    private int status;
      @JsonInclude(JsonInclude.Include.NON_NULL)
    private Article article;

    public ArticleResponse(String message, int status) {
        super(message,status);
    }

    public ArticleResponse(Article article,String message, int status) {
        super(message,status);
        this.article = article;
//        this.statusCode = statusCode;
    }
}
