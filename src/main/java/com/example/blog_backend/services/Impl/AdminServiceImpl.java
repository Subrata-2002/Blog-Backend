package com.example.blog_backend.services.Impl;

import com.example.blog_backend.controller.MasterResponseBody;
import com.example.blog_backend.entity.Admin;
import com.example.blog_backend.repository.AdminRepository;
import com.example.blog_backend.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;
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
    public MasterResponseBody<String> loginUser(String logindata, String pwd) {


        return null;
    }

}
