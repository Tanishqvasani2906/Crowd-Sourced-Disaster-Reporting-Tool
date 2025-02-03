package com.example.Disaster_Management_Tool.Controllers;

import com.example.Disaster_Management_Tool.Dto.LoginRequest;
import com.example.Disaster_Management_Tool.Dto.LoginResponse;
import com.example.Disaster_Management_Tool.Dto.UserRequest;
import com.example.Disaster_Management_Tool.Entities.Role;
import com.example.Disaster_Management_Tool.Entities.User;
import com.example.Disaster_Management_Tool.Repositories.UserRepo;
import com.example.Disaster_Management_Tool.Services.JWTService;
//import com.example.Disaster_Management_Tool.Services.OTPService;
import com.example.Disaster_Management_Tool.Services.OTPService;
import com.example.Disaster_Management_Tool.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://127.0.0.1:3000 , https://crisis-connect-nine.vercel.app , https://dhruvasetu.vercel.app"})
@RequestMapping("/userlogin")
public class AuthController {
    @Autowired
    private UserService service;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OTPService otpService; // Service to handle OTP generation and validation

    @PostMapping("/sendOtp")
    public ResponseEntity<?> sendOtp(@RequestParam String phoneNumber) {
        try {
            // Validate phone number format
            if (!isValidPhoneNumber(phoneNumber)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid phone number format. Please check and try again.");
            }

            // Check if phone number exists in the database
            if (!userRepo.existsByPhoneNumber(phoneNumber)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Phone number is already registered");
            }

            // Generate and send OTP
            otpService.sendOtp(phoneNumber);
            return ResponseEntity.ok("OTP sent successfully to " + phoneNumber);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP. Please try again.");
        }
    }


    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        try {
            // Validate phone number format
            if (!isValidPhoneNumber(phoneNumber)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid phone number format. Please check and try again.");
            }

            boolean isVerified = otpService.verifyOtp(phoneNumber, otp);

            if (isVerified) {
                return ResponseEntity.ok("OTP verified successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP. Please try again.");
            }
        } catch (Exception e) {
            // Log the exception (for internal debugging)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while verifying OTP. Please try again.");
        }
    }

    // Helper method to validate phone number format
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Example: You can use a regular expression to validate phone number format.
        String regex = "^\\+?[1-9]\\d{1,14}$"; // E.164 format
        return phoneNumber.matches(regex);
    }


    //@PostMapping("/register")
//public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
//    try {
//        // Convert UserRequest to User entity
//        User user = new User();
//
//        user.setUsername(userRequest.getUsername());
//        user.setPassword(userRequest.getPassword()); // Make sure to hash the password before saving (use a password encoder)
//        user.setEmail(userRequest.getEmail());
//        user.setRole(userRequest.getRole() != null ? userRequest.getRole() : Role.USER); // Default to 'USER' if role is not provided
//        user.setLocation(userRequest.getLocation());  // Setting the location
//        user.setLatitude(userRequest.getLatitude());  // Setting latitude
//        user.setLongitude(userRequest.getLongitude());  // Setting longitude
//        user.setUpdatedAt(LocalDateTime.now());
//        user.setCreatedAt(LocalDateTime.now());
//        user.setPhoneNumber(userRequest.getPhoneNumber());
//
//        // Register user
//        User registeredUser = service.register(user);
//
//        return ResponseEntity.ok(registeredUser);
//    } catch (DataIntegrityViolationException e) {
//        // Handle the case where the username is already taken
//        return ResponseEntity.status(HttpStatus.CONFLICT)
//                .body("Error: Username is already taken!");
//    } catch (Exception e) {
//        // Handle other exceptions
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("An unexpected error occurred. Please try again later.");
//    }
//}
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Validate required fields
            if (user.getUsername() == null || user.getPhoneNumber() == null || user.getLocation() == null
                    || user.getLatitude() == null || user.getLongitude() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("All required fields (username, phoneNumber, location, latitude, longitude) must be provided!");
            }

            // Ensure the role is set, defaulting to USER if not provided
            user.setRole(user.getRole() != null ? user.getRole() : Role.USER);

            // Register the user (id is auto-generated)
            User registeredUser = service.register(user);

            return ResponseEntity.ok(registeredUser);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data integrity violation: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register user. Please try again. Error: " + e.getMessage());
        }
    }





    //    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody UserRequest userRequest) {
//        // Convert UserRequest to User entity
//        User user = new User();
//        user.setUsername(userRequest.getUsername());
//        user.setPassword(userRequest.getPassword()); // Ensure you use the same hashing mechanism for verification
//
//
//
//        String token = service.verify(user);
//        String username = userRequest.getUsername();
//
//        LoginResponse loginResponse = new LoginResponse(token, username);
//
//        return ResponseEntity.ok(loginResponse);
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validate input
            if (loginRequest.getPhoneNumber() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Phone number is required for login!");
            }

            // Check if user exists
            User user = service.findByPhoneNumber(loginRequest.getPhoneNumber());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with this phone number does not exist!");
            }

            // Generate JWT token
            String token = jwtService.generateToken(user.getId(),user.getUsername(), user.getPhoneNumber() ,user.getRole().name() );

            // Create response object
            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .userId(user.getId())
                    .username(user.getUsername()) // Assuming username is available in User entity
                    .role(user.getRole().name())
                    .phoneNumber(user.getPhoneNumber())
                    .build();

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process login. Please try again.");
        }
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
