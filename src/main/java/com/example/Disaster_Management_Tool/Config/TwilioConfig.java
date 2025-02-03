package com.example.Disaster_Management_Tool.Config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    // Load .env file
    private final Dotenv dotenv = Dotenv.load();

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromWhatsAppNumber;

    @Value("${twilio.phone.number}")
    private String phoneNumber;

    // Constructor to load environment variables dynamically
    public TwilioConfig() {
        // Set values from .env to system properties
        System.setProperty("TWILIO_ACCOUNT_SID", dotenv.get("TWILIO_ACCOUNT_SID"));
        System.setProperty("TWILIO_AUTH_TOKEN", dotenv.get("TWILIO_AUTH_TOKEN"));
        System.setProperty("TWILIO_WHATSAPP_FROM", dotenv.get("TWILIO_WHATSAPP_FROM"));
        System.setProperty("TWILIO_PHONE_NUMBER", dotenv.get("TWILIO_PHONE_NUMBER"));
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