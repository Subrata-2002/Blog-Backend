package com.example.blog_backend.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.blog_backend.services.Impl.AdminDetailsServiceImpl;
import com.example.blog_backend.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils; // A utility class for handling JWT operations

    @Autowired
    private AdminDetailsServiceImpl adminDetailsServiceimpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException, ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("url is "+requestURI);

//         Skip JWT validation for public endpoint
//        if ("/api/admin/register".equals(requestURI)) {
//
//            System.out.println("Public route is this");
//            chain.doFilter(request, response);
//            return;
//        }
        System.out.println("/api/admin/article/public".equals(requestURI));

        if ("/api/admin/article/public".equals(requestURI)) {

            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
      if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            if(requestURI.startsWith("/api/admin") && !"/api/admin/register".equals(requestURI) && !"/api/admin/public".equals(requestURI) && !"/api/admin/login".equals(requestURI) && !"/api/admin/article/public".equals(requestURI)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Authorization token is required\", \"status\": \"401\"}");
                return; // Stop further processing
            }
            chain.doFilter(request, response);
            return;
        }


        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
              jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtils.extractUsername(jwt);
            } catch (TokenExpiredException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                System.out.println("Token Expired TokenExpiredException");
                response.getWriter().write("{\"message\": \"Token expired!! Please Login Again\", \"status\": \"401\"}");
                return; // Stop further processing
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                System.out.println("Token Expired Exception");
                response.getWriter().write("{\"message\": \"Invalid token\", \"status\": \"401\"}");
                return; // Stop further processing
            }

            System.out.println("Extracted username: " + username);

        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.print("username in the JwtRequestFilter is " + username);

            UserDetails userDetails = this.adminDetailsServiceimpl.loadUserByUsername(username);
            System.out.print("userdetails is" + userDetails);

            if (jwtUtils.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}