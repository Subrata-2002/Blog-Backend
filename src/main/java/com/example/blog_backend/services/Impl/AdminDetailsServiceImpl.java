package com.example.blog_backend.services.Impl;

import com.example.blog_backend.entity.Admin;
import com.example.blog_backend.repository.AdminRepository;
import com.example.blog_backend.services.AdminDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AdminDetailsServiceImpl implements AdminDetailsService{

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(admin.getUsername(), admin.getPwd(), new ArrayList<>());
    }
}
