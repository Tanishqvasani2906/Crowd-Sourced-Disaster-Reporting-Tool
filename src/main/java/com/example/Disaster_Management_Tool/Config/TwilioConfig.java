package com.example.Disaster_Management_Tool.Config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    // Load .env file
    private Dotenv dotenv = Dotenv.load();

    @Value("${TWILIO_ACCOUNT_SID}")
    private String accountSid;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String authToken;

    @Value("${TWILIO_WHATSAPP_FROM}")
    private String fromWhatsAppNumber;

    @Value("${TWILIO_PHONE_NUMBER}")
    private String phoneNumber;

    // Constructor to load environment variables dynamically
    public TwilioConfig() {
        try {
            dotenv = Dotenv.load();
            System.setProperty("TWILIO_ACCOUNT_SID", dotenv.get("TWILIO_ACCOUNT_SID"));
            System.setProperty("TWILIO_AUTH_TOKEN", dotenv.get("TWILIO_AUTH_TOKEN"));
            System.setProperty("TWILIO_WHATSAPP_FROM", dotenv.get("TWILIO_WHATSAPP_FROM"));
            System.setProperty("TWILIO_PHONE_NUMBER", dotenv.get("TWILIO_PHONE_NUMBER"));
        } catch (DotenvException e) {
            // Handle the case where .env file is not found
            // Perhaps log a warning or use default values
            System.err.println("Could not load .env file: " + e.getMessage());
        }
    }


    // Getter methods
    public String getAccountSid() {
        return accountSid != null ? accountSid : System.getProperty("TWILIO_ACCOUNT_SID");
    }

    public String getAuthToken() {
        return authToken != null ? authToken : System.getProperty("TWILIO_AUTH_TOKEN");
    }

    public String getFromWhatsAppNumber() {
        return fromWhatsAppNumber != null ? fromWhatsAppNumber : System.getProperty("TWILIO_WHATSAPP_FROM");
    }
    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber : System.getProperty("TWILIO_PHONE_NUMBER");
    }
}
