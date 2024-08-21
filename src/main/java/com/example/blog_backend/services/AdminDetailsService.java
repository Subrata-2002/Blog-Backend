package com.example.blog_backend.services;
import org.springframework.security.core.userdetails.UserDetails;

public interface AdminDetailsService {
    /**
     * Loads the user associated with the given username.
     *
     * @param username the username identifying the user whose data is required.
     * @return the user details for the user with the given username.
    //     * @throws UsernameNotFoundException if the user could not be found.
     */
    UserDetails loadUserByUsername(String username) ;
}


