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
        // Get values from .env (if available) or system environment variables
        String accountSid = System.getenv("TWILIO_ACCOUNT_SID");
        String authToken = System.getenv("TWILIO_AUTH_TOKEN");
        String whatsappFrom = System.getenv("TWILIO_WHATSAPP_FROM");
        String phoneNumber = System.getenv("TWILIO_PHONE_NUMBER");

        // If running locally and dotenv is available, use it as a fallback
        Dotenv dotenv;
        try {
            dotenv = Dotenv.configure().ignoreIfMissing().load(); // Only works if dotenv is present
            accountSid = accountSid != null ? accountSid : dotenv.get("TWILIO_ACCOUNT_SID");
            authToken = authToken != null ? authToken : dotenv.get("TWILIO_AUTH_TOKEN");
            whatsappFrom = whatsappFrom != null ? whatsappFrom : dotenv.get("TWILIO_WHATSAPP_FROM");
            phoneNumber = phoneNumber != null ? phoneNumber : dotenv.get("TWILIO_PHONE_NUMBER");
        } catch (Exception e) {
            System.out.println("Dotenv not found, using only system environment variables.");
        }

        // Set the final values
        System.setProperty("TWILIO_ACCOUNT_SID", accountSid);
        System.setProperty("TWILIO_AUTH_TOKEN", authToken);
        System.setProperty("TWILIO_WHATSAPP_FROM", whatsappFrom);
        System.setProperty("TWILIO_PHONE_NUMBER", phoneNumber);
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