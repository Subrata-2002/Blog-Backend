package com.example.blog_backend.controller;


import com.example.blog_backend.dto.LoginDto;
import com.example.blog_backend.entity.Admin;
import com.example.blog_backend.services.AdminService;
import com.example.blog_backend.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@RestController
@RequestMapping("/api/user")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<MasterResponseBody<Admin>> createUser(@RequestBody Admin admin){
        // Validate user data before saving
        MasterResponseBody<Admin> savedUser = adminService.createUser(admin);
        HttpStatus status;
        switch (savedUser.getStatus()) {
            case 500:
                status = HttpStatus.NOT_IMPLEMENTED;
                break;
            default:
                status = HttpStatus.OK;
        }
        return new ResponseEntity<>(savedUser, status);
    }


    @PostMapping("/login")
    public ResponseEntity<MasterResponseBody<String>> loginUser(@RequestBody LoginDto logindto){
//        String token = String.valueOf(userService.loginUser(logindto.getLogindata(), logindto.getPwd()));
        System.out.println("welcome to login");
        MasterResponseBody<String> bb= adminService.loginUser(logindto.getLogindata(), logindto.getPwd());
        return new ResponseEntity<>(bb, HttpStatusCode.valueOf(bb.getStatus()));
    };
}
