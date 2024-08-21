package com.example.blog_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @NotEmpty(message = "Username is required.")
    private String username;

    @Column(name ="email", nullable = false, unique = true)
    @NotEmpty(message = "Email is required.")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Please give a valid email id.")
    private String email;

    public void setEmail(String email){
        this.email = email.toLowerCase();
    }

    @Column(name ="password", nullable = false)
    @NotEmpty(message = "Password is required.")
    private String pwd;
}
