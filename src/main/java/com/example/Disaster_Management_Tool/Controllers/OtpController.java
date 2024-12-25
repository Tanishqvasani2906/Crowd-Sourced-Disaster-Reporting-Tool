package com.example.Disaster_Management_Tool.Controllers;
import com.example.Disaster_Management_Tool.Dto.EmailVerifyRequest;
import com.example.Disaster_Management_Tool.Dto.OtpSentRequest;
import com.example.Disaster_Management_Tool.Entities.User;
import com.example.Disaster_Management_Tool.Repositories.UserRepo;
import com.example.Disaster_Management_Tool.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepository;

    // In-memory store for OTPs; for production, consider using a database
    private static Map<String, String> otpStore = new HashMap<>();

    @PostMapping("/send")
    public ResponseEntity<String> sendOTP(@RequestBody OtpSentRequest otpSentReq) {
        String email = otpSentReq.getEmail();

        // Fetch the authenticated user's email
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Email not found in the database.");
        }

        // Generate a random 4-digit OTP
        String otp = String.format("%04d", new Random().nextInt(9999));

        // Store OTP associated with the email
        otpStore.put(email, otp);

        // Send the OTP to the provided email
        emailService.sendOTP(email, otp);

        // Return success response
        return ResponseEntity.ok("OTP sent successfully to " + email);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOTP(@RequestBody EmailVerifyRequest emailVerifyRequest) {
        String email = emailVerifyRequest.getEmail();
        String otp = emailVerifyRequest.getOtp();

        // Fetch the authenticated user's email
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Email not found in the database.");
        }

        // Get the stored OTP for the given email
        String storedOTP = otpStore.get(email);

        // Check if the provided OTP matches the stored OTP
        if (storedOTP != null && storedOTP.equals(otp)) {
            // OTP is correct, remove it from the store
            otpStore.remove(email);
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            // OTP is incorrect
            return ResponseEntity.status(400).body("Invalid OTP.");
        }
    }
}
