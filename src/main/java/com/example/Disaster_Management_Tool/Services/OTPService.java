package com.example.Disaster_Management_Tool.Services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class OTPService {

    // Load Twilio credentials from the environment
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.sms.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    // In-memory cache for OTPs (can be replaced with Redis or database for production)
    private Map<String, String> otpCache = new HashMap<>();
    private Map<String, Long> otpExpirationCache = new HashMap<>(); // To track OTP expiration

    public OTPService() {
        try {
            // Initialize Twilio with credentials
            if (accountSid == null || authToken == null) {
                throw new IllegalArgumentException("Twilio credentials are missing.");
            }
            Twilio.init(accountSid, authToken);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error initializing Twilio: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Method to send OTP to the provided phone number
    public void sendOtp(String phoneNumber) {
        String otp = generateOtp();
        otpCache.put(phoneNumber, otp);

        // Set OTP expiration time (5 minutes)
        otpExpirationCache.put(phoneNumber, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));

        // Send OTP via Twilio
        try {
            Message message = Message.creator(
                            new PhoneNumber(phoneNumber), // To phone number
                            new PhoneNumber(twilioPhoneNumber), // From Twilio number
                            "Your OTP is: " + otp) // Message body
                    .create();

            System.out.println("OTP sent to: " + phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send OTP");
        }
    }

    // Method to verify OTP
    public boolean verifyOtp(String phoneNumber, String enteredOtp) {
        String storedOtp = otpCache.get(phoneNumber);
        Long expirationTime = otpExpirationCache.get(phoneNumber);

        // Check if the OTP exists and is not expired
        if (storedOtp != null && expirationTime != null && System.currentTimeMillis() < expirationTime) {
            if (storedOtp.equals(enteredOtp)) {
                // OTP is valid, remove it from cache after successful verification
                otpCache.remove(phoneNumber);
                otpExpirationCache.remove(phoneNumber);
                return true; // OTP verified successfully
            } else {
                return false; // OTP doesn't match
            }
        } else {
            otpCache.remove(phoneNumber); // Remove expired OTP from cache
            otpExpirationCache.remove(phoneNumber); // Remove expired OTP expiration time
            return false; // OTP has expired or doesn't exist
        }
    }

    // OTP generation logic (6-digit OTP)
    private String generateOtp() {
        // You can customize the OTP length here
        int otp = (int) (Math.random() * 900000) + 100000; // 6-digit OTP
        return String.valueOf(otp);
    }
}
