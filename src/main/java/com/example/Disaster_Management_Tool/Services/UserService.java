package com.example.Disaster_Management_Tool.Services;

import com.example.Disaster_Management_Tool.Entities.User;
import com.example.Disaster_Management_Tool.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(user.getPassword())); // Hash only if password is provided
        }
        return repo.save(user);
    }


//    public String verify(User user) {
//        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//        if (authentication.isAuthenticated()) {
//            return jwtService.generateToken(user.getUsername());
//        } else {
//            return "fail";
//        }
//    }
//public String verify(User user) {
//    // Authenticate the user
//    Authentication authentication = authManager.authenticate(
//            new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
//    );
//
//    // Check if the authentication is successful
//    if (authentication.isAuthenticated()) {
//        // Generate JWT token using the authenticated user's username
//        String token = jwtService.generateToken(user.getUsername(), String.valueOf(user.getRole()), user.getId());
//
//        return token;
//    } else {
//        // Return failure message if authentication fails
//        return "fail";
//    }
//}


    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public User findById(String userId) {
        return repo.findById(userId).orElse(null);
    }

    public User findByPhoneNumber(String phoneNumber) {
        return (User) repo.findByPhoneNumber(phoneNumber)
                .orElse(null); // Return null if no user is found
    }

}
