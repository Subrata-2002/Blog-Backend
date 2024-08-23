package com.example.blog_backend.config;

import com.example.blog_backend.filter.JwtRequestFilter;
import com.example.blog_backend.repository.AdminRepository;
import com.example.blog_backend.services.AdminService;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private JwtRequestFilter jwtRequestFilter;
    private AdminService adminService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, AdminService adminService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.adminService = adminService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // Disable CSRF protection for simplicity
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/api/admin/**").permitAll()// Allow access to public endpoints
                                .requestMatchers("/api/admin/create").authenticated()
                                .anyRequest().authenticated()  // Require authentication for all other endpoints
                )
//                .httpBasic(Customizer.withDefaults());  // Use HTTP Basic authentication
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        System.out.println("SecurityFilterChain created");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
