package com.example.blog_backend.services.Impl;

import com.example.blog_backend.controller.LoginResponse;
import com.example.blog_backend.controller.MasterResponseBody;
import com.example.blog_backend.entity.Admin;
import com.example.blog_backend.entity.Article;
import com.example.blog_backend.repository.AdminRepository;
import com.example.blog_backend.repository.ArticleRepository;
import com.example.blog_backend.response.ArticleResponse;
import com.example.blog_backend.services.AdminService;
import com.example.blog_backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ArticleRepository articleRepository;


    @Override
    public MasterResponseBody<Admin> createUser(Admin admin) {
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(admin.getPwd());

            admin.setPwd(encodedPassword);
            adminRepository.save(admin);
            return new MasterResponseBody<Admin>("User created successfully", 200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new MasterResponseBody<>("Error creating user", 500);
        }
    }

    @Override
    public LoginResponse loginUser(String logindata, String pwd) {
        try {
            Optional<Admin> userOptional;

            if (logindata.contains("@")) {
                userOptional = adminRepository.findByEmail(logindata.toLowerCase());
            } else {
                userOptional = adminRepository.findByUsername(logindata);
            }
            // If user is not found, return appropriate response
            if (!userOptional.isPresent()) {

                return new LoginResponse("User not found", 404);
            }
            Admin admin = userOptional.get();

            // Check if the provided password matches the stored password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (passwordEncoder.matches(pwd, admin.getPwd())) {
                String jwtToken = jwtUtils.generateToken(admin.getUsername());
//                String jwtRefreshToken = jwtUtils.generateRefreshToken(admin.getUsername());

                System.out.println("here the user.getUsername() data is : " + admin.getUsername());
                System.out.println("Jwt Token is: " + jwtToken);
//                System.out.println("Jwt Refresh Token is: " + jwtRefreshToken);

                return new LoginResponse(jwtToken, "Login successful", 200);

            } else {
                return new LoginResponse("Invalid password", 401);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LoginResponse("An unexpected error occurred: " + e.getMessage(), 501);
        }
    }

    @Override
    public MasterResponseBody<String> createArticle(Article article, MultipartFile heroImage) {
        try {
            String slug = generateSlug(article.getTitle());
            article.setTitle(slug);

            // Save the image to the local file system
            String imagePath = saveImage(heroImage);
            article.setHeroImage(imagePath);

            // Save the article to the database
            articleRepository.save(article);

            return new MasterResponseBody<>("Article created successfully", 200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new MasterResponseBody<>("Error creating article", 500);
        }
    }

    private String saveImage(MultipartFile image) {
        final String IMAGE_DIRECTORY = "images/"; // Change to your desired directory
        try {
            // Create the directory if it doesn't exist
            File directory = new File(IMAGE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create the file path
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(IMAGE_DIRECTORY + fileName);

            // Save the image to the file system
            Files.write(filePath, image.getBytes());

            System.out.println("Filepath is " + filePath.toString());
            System.out.println("FileName is " + fileName);
            return filePath.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    // Method to generate slug
    private String generateSlug(String title) {
        // Step 1: Normalize the title (convert to lowercase and remove special characters)
        String normalizedTitle = title.toLowerCase().replaceAll("[^a-z0-9\\s]", "").replaceAll("\\s+", "-");
        System.out.println("normalizedTitle is " + normalizedTitle);
        // Step 2: Generate a unique identifier using SecureRandom
        String uniqueIdentifier = generateRandomString(12); // Adjust the length as needed

        System.out.println("uniqueIdentifier is " + uniqueIdentifier);
        // Step 3: Concatenate the normalized title with the unique identifier
        return normalizedTitle + "-" + uniqueIdentifier;
    }

    // Method to generate a random string for uniqueness
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder randomString = new StringBuilder(length);
        System.out.println("random string is " + randomString);

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            System.out.println("randomIndex is " + randomIndex);
            randomString.append(characters.charAt(randomIndex));
        }
        System.out.println("random string is in next part " + randomString.toString());
        return randomString.toString();
    }


    @Override
    public List<Article> getAllPublicArticles() {
        System.out.println("In public article list");

        return articleRepository.findByIsPublicTrue();
    }

    @Override
    public List<Article> getAllPrivateArticles() {
        try {
            System.out.println("In private article list");
            return articleRepository.findByIsPublicFalse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return (List<Article>) new MasterResponseBody<>("Error creating user", 500);
        }

    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

//    @Override
//    public Article findPublicArticleById(Long id) {
//        System.out.println("findPublicArticleById");
//        Article article = findById(id);
//
//        System.out.println("article is "+article.isPublic());
//        System.out.println("Article found"+article.toString());
//        if (!article.isPublic()) {
//            return new Article("article is not public", 403);
//        }
//        return article;
//    }
    @Override
    public ArticleResponse findPublicArticleById(Long id){
        System.out.println("findPublicArticleById");
        Article article = findById(id);

        System.out.println("article is "+article.isPublic());
        System.out.println("Article found "+article.toString());

        if(!article.isPublic()){
            return new ArticleResponse("Article is private and cannot be accessed without authentication", 403);
        }
        return new ArticleResponse(article,"article fetched successfully",200);
    }


    @Override
    public ArticleResponse findPrivateArticleById(Long id) {
        System.out.println("findPublicArticleById");
        Article article = findById(id);
        if(article.isPublic()){
            return new ArticleResponse("Article is public and cannot be accessed without authentication", 403);
        }
        return new ArticleResponse(article,"article fetched successfully",200);
    }

    @Override
    public MasterResponseBody<String> updateArticle(Long id, Article updatedArticle, MultipartFile heroImage) {
        // Fetch the article by ID
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        // Update the article fields
        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setHtmlContent(updatedArticle.getHtmlContent());
        existingArticle.setPublic(updatedArticle.isPublic());

        // If heroImage is provided, handle the file upload and update
        if (heroImage != null && !heroImage.isEmpty()) {
            // Handle file upload logic and update heroImage in the article
            String imageUrl = FileUploadService.uploadFile(heroImage);
            System.out.println("ImageUrl is "+imageUrl);
            existingArticle.setHeroImage(imageUrl);
        }

        // Save the updated article back to the database
        articleRepository.save(existingArticle);

        // Return a success response
        return new MasterResponseBody<>("Article updated successfully",200);
    }

    @Service
    static
    class FileUploadService {

        private static final String uploadDirectory = "uploads/";

        public static String uploadFile(MultipartFile file) {
            try {
                // Ensure the directory exists
                Path uploadPath = Paths.get(uploadDirectory);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a file name and save the file
                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);

                return filePath.toString(); // Return the file path
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }
    }


}


