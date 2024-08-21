package com.example.blog_backend.services;

import com.example.blog_backend.controller.MasterResponseBody;
import com.example.blog_backend.entity.Admin;

public interface AdminService {
    MasterResponseBody<Admin> createUser(Admin admin);
    MasterResponseBody<String> loginUser(String Logindata, String pwd);
}
