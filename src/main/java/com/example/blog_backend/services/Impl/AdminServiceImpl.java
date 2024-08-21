package com.example.blog_backend.services.Impl;

import com.example.blog_backend.controller.LoginResponse;
import com.example.blog_backend.controller.MasterResponseBody;
import com.example.blog_backend.entity.Admin;
import com.example.blog_backend.repository.AdminRepository;
import com.example.blog_backend.services.AdminService;
import com.example.blog_backend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtils jwtUtils;
//    private final JwtUtil jwtUtil;

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
    public LoginResponse loginUser(String logindata, String pwd){

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

                return new LoginResponse(jwtToken ,"Login successful", 200);

            } else {
                return new LoginResponse("Invalid password", 401);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LoginResponse("An unexpected error occurred: " + e.getMessage(), 501);
        }
    }

}
