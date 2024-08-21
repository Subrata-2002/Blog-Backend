package com.example.blog_backend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils{
    @Value("${SECRET_KEY}")
    private String secretkey;// Use a secure key

//    @Autowired
//    private BlackList blackList;

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 )) // 10 hours
                .withClaim("type", "access")
                .sign(Algorithm.HMAC256(secretkey));
    }
    public String generateRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7  )) // 7 days
                .withClaim("type", "refresh") // Add type claim
                .sign(Algorithm.HMAC256(secretkey));
    }

    public String extractUsername(String token) {
        String username = getDecodedJWT(token).getSubject();
        System.out.println("Extracted username from extractUsername: " + username);
        return getDecodedJWT(token).getSubject();
    }

    public boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            String tokenType = getTokenType(token);

            if ("access".equals(tokenType)) {
                return validateAccessToken(extractedUsername, username, token);
            } else if ("refresh".equals(tokenType)) {
                return validateRefreshToken(extractedUsername, username, token);
            }
            return false;
        } catch(JWTVerificationException e) {
            return false;
        }
    }

    public boolean validateAccessToken(String extractedUsername, String username, String token) {
        System.out.println("checking1: " + extractedUsername);
//        System.out.println("checking2: " +(extractedUsername.equals(username) && !isTokenExpired(token) && blackList.isTokenBlacklisted(token)));
//        return (extractedUsername.equals(username) && !isTokenExpired(token) && !blackList.isTokenBlacklisted(token));
        System.out.println("checking2: " +(extractedUsername.equals(username) && !isTokenExpired(token) ));
        return (extractedUsername.equals(username) && !isTokenExpired(token) );
    }

    public boolean validateRefreshToken(String extractedUsername, String username, String token) {
        // Implement specific validation logic for refresh tokens if needed
        return (extractedUsername.equals(username) && isTokenExpired(token));
    }

    public String getTokenType(String token) {
        return getDecodedJWT(token).getClaim("type").asString();
    }

    public boolean isTokenExpired(String token) {
//        This checks if the expiration date is before the current date
        return getDecodedJWT(token).getExpiresAt().before(new Date()); // returns true if token is expired
    }

    private DecodedJWT getDecodedJWT(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretkey)).build();
        return verifier.verify(token);
    }
}
