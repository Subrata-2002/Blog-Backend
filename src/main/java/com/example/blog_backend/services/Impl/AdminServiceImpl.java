package com.example.blog_backend.services.Impl;

import com.example.blog_backend.controller.LoginResponse;
import com.example.blog_backend.controller.MasterResponseBody;
import com.example.blog_backend.entity.Admin;
import com.example.blog_backend.entity.Article;
import com.example.blog_backend.repository.AdminRepository;
import com.example.blog_backend.repository.ArticleRepository;
import com.example.blog_backend.services.AdminService;
import com.example.blog_backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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


    //    @Override
//    public String createArticle (String path, MultipartFile file ) throws IOException {
//
//        String name = file.getOriginalFilename();
//
//        String filePath = path +File.separator+name;
//
//        File f = new File(filePath);
//
//        if(!f.exists()) {
//            f.mkdir();
//        }
//
//        Files.copy(file.getInputStream(),Paths.get(filePath));
//
//        return name;
//    }

    @Override
    public MasterResponseBody<String> createArticle(Article article, MultipartFile heroImage) {
        // Save the image to the local file system
        String imagePath = saveImage(heroImage);

        // Set the image path and save the article to the database
        article.setHeroImage(imagePath.toString());
        System.out.println("Image path"+imagePath);
        articleRepository.save(article);

        return new MasterResponseBody<>("Article created successfully", 200);
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

            // Return the path relative to the image directory

            System.out.println("Filepath is " + filePath.toString());
            System.out.println("FileName is " + fileName);
            return filePath.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }
}


