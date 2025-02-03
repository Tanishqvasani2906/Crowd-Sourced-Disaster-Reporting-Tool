package com.example.Disaster_Management_Tool.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JWTService {

    private String secretkey = "";

    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Generate the token with userId, role, and phone number
//    public String generateToken(String username, String role, String userId, String phoneNumber) {
//        System.out.println("Generating token with role: " + role);  // Log role before generating token
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("role", role);  // Add role to the claims
//        claims.put("id", userId);  // Add user ID to the claims
//        claims.put("phoneNumber", phoneNumber);  // Add phone number to the claims
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000)) // Token expiry time
//                .signWith(SignatureAlgorithm.HS256, secretkey) // Sign with the secret key
//                .compact();
//    }
    // Generate the token with correct mappings
    public String generateToken(String userId, String username, String phoneNumber, String role) {
        System.out.println("Generating token with role: " + role);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);  // ✅ Role should be role
        claims.put("phoneNumber", phoneNumber);  // ✅ Phone number should be phone number
        claims.put("id", userId);  // ✅ ID should be user ID
        claims.put("username", username);  // ✅ Username should be username

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)  // ✅ Subject (sub) should be username
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(90))) // 3 months (assuming 30 days per month)
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .compact();
    }


    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }
//    public String extractUserName(String token) {
//        // ✅ Extract username using the correct claim (phoneNumber)
//        return extractClaim(token, claims -> claims.get("phoneNumber", String.class));
//    }


    // Extract the 'role' claim from the token
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

//     Extract the 'id' claim from the token
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("id", String.class));
    }

//     Extract the 'phoneNumber' claim from the token
    public String extractPhoneNumber(String token) {
        return extractClaim(token, claims -> claims.get("phoneNumber", String.class));
    }



    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())  // Set the signing key
                .build()                  // Build the parser
                .parseSignedClaims(token) // Parse the signed claims
                .getPayload();            // Get the payload (claims)
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        final String role = extractRole(token);  // Extract role from token
        final String userId = extractUserId(token);  // Extract userId from token
        final String phoneNumber = extractPhoneNumber(token);  // Extract phone number from token

        // You can now use the role, userId, and phoneNumber for additional checks if needed
        // For example, check if the user role is valid or if the phone number matches:
        if (role.equals("ADMIN")) {
            // Additional logic for admin users, if needed
        }

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String phoneNumber = extractPhoneNumber(token);  // Extract phone number
//        return (phoneNumber.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Logout functionality
    private Set<String> blacklistedTokens = new HashSet<>();

    // Add the token to the blacklist
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Check if the token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
