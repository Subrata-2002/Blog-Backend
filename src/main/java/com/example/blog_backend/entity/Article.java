package com.example.blog_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles")
public class Article{
    // Add your article entity attributes here
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "title", nullable = false, unique = true,columnDefinition = "TEXT")
    @NotEmpty(message = "Title is required.")
    private String title;

    @Column(name = "content", nullable = false)
    @NotEmpty(message = "Content is required.")
    private String htmlContent;

    @Column(name = "image", nullable = false )
    @NotEmpty(message = "Hero-Image is required.")
    private String heroImage;

    @Column(name = "accessType" , nullable = false)
    private boolean isPublic = false; // Default to private (false)

}
