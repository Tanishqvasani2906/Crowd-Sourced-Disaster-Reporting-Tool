package com.example.Disaster_Management_Tool.Controllers;

import com.example.Disaster_Management_Tool.Dto.LoginResponse;
import com.example.Disaster_Management_Tool.Dto.UserRequest;
import com.example.Disaster_Management_Tool.Entities.User;
import com.example.Disaster_Management_Tool.Services.JWTService;
import com.example.Disaster_Management_Tool.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/userlogin")
public class AuthController {
    @Autowired
    private UserService service;
    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRequest userRequest) {
        // Convert UserRequest to User entity
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword()); // Ensure you hash the password before saving
        user.setEmail(userRequest.getEmail());

        // Register user
        User registeredUser = service.register(user);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserRequest userRequest) {
        // Convert UserRequest to User entity
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword()); // Ensure you use the same hashing mechanism for verification

        String token = service.verify(user);
        String username = userRequest.getUsername();
        LoginResponse loginResponse = new LoginResponse(token, username);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Extract JWT token from the Authorization header
        String token = extractTokenFromRequest(request);

        // Blacklist the token if token blacklisting is used
        if (token != null) {
            jwtService.blacklistToken(token);
        }

        // Invalidate session (if you're also using sessions)
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("Logged out successfully");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}



//package com.example.Disaster_Management_Tool.Controllers;
//
//import com.example.Disaster_Management_Tool.Dto.LoginResponse;
//import com.example.Disaster_Management_Tool.Entities.User;
//
//import com.example.Disaster_Management_Tool.Services.JWTService;
//import com.example.Disaster_Management_Tool.Services.UserService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@CrossOrigin(origins = "http://127.0.0.1:5173")
//@RequestMapping("/userlogin")
//public class AuthController {
//    @Autowired
//    private UserService service;
//    @Autowired
//    private JWTService jwtService;
//
//
//    @PostMapping("/register")
//    public User register(@RequestBody User user) {
//        return service.register(user);
//
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
//        String token = service.verify(user);
//        String username = user.getUsername();
//        LoginResponse loginResponse = new LoginResponse(token,username);
//        return ResponseEntity.ok(loginResponse);
//    }
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        // Extract JWT token from the Authorization header
//        String token = extractTokenFromRequest(request);
//
//        // Blacklist the token if token blacklisting is used
//        if (token != null) {
//            jwtService.blacklistToken(token);
//        }
//
//        // Invalidate session (if you're also using sessions)
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//
//        return ResponseEntity.ok("Logged out successfully");
//    }
//
//    private String extractTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//}
