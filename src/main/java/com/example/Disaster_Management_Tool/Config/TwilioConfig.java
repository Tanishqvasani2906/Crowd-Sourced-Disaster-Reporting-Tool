package com.example.Disaster_Management_Tool.Config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class TwilioConfig {

    // Logger instance for logging
    private static final Logger logger = LoggerFactory.getLogger(TwilioConfig.class);

    // Load .env file
    private Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

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
        String accountSid = System.getenv("TWILIO_ACCOUNT_SID");
        String authToken = System.getenv("TWILIO_AUTH_TOKEN");
        String whatsappFrom = System.getenv("TWILIO_WHATSAPP_FROM");
        String phoneNumber = System.getenv("TWILIO_PHONE_NUMBER");

        try {
            dotenv = Dotenv.configure().ignoreIfMissing().load(); // Only works if dotenv is present
            accountSid = accountSid != null ? accountSid : dotenv.get("TWILIO_ACCOUNT_SID");
            authToken = authToken != null ? authToken : dotenv.get("TWILIO_AUTH_TOKEN");
            whatsappFrom = whatsappFrom != null ? whatsappFrom : dotenv.get("TWILIO_WHATSAPP_FROM");
            phoneNumber = phoneNumber != null ? phoneNumber : dotenv.get("TWILIO_PHONE_NUMBER");

            // Log successful loading of environment variables
            logger.info("Twilio configuration loaded successfully.");
        } catch (Exception e) {
            // Log the exception if dotenv fails
            logger.error("Dotenv not found, using only system environment variables.", e);
        }

        // Set the final values
        if (accountSid != null) {
            System.setProperty("TWILIO_ACCOUNT_SID", accountSid);
        } else {
            logger.warn("TWILIO_ACCOUNT_SID is not set.");
        }

        if (authToken != null) {
            System.setProperty("TWILIO_AUTH_TOKEN", authToken);
        } else {
            logger.warn("TWILIO_AUTH_TOKEN is not set.");
        }

        if (whatsappFrom != null) {
            System.setProperty("TWILIO_WHATSAPP_FROM", whatsappFrom);
        } else {
            logger.warn("TWILIO_WHATSAPP_FROM is not set.");
        }

        if (phoneNumber != null) {
            System.setProperty("TWILIO_PHONE_NUMBER", phoneNumber);
        } else {
            logger.warn("TWILIO_PHONE_NUMBER is not set.");
        }
    }

    // Getter methods
    public String getAccountSid() {
        String sid = accountSid != null ? accountSid : System.getProperty("TWILIO_ACCOUNT_SID");
        if (sid == null) {
            logger.error("TWILIO_ACCOUNT_SID is not available.");
        }
        return sid;
    }

    public String getAuthToken() {
        String token = authToken != null ? authToken : System.getProperty("TWILIO_AUTH_TOKEN");
        if (token == null) {
            logger.error("TWILIO_AUTH_TOKEN is not available.");
        }
        return token;
    }

    public String getFromWhatsAppNumber() {
        String from = fromWhatsAppNumber != null ? fromWhatsAppNumber : System.getProperty("TWILIO_WHATSAPP_FROM");
        if (from == null) {
            logger.error("TWILIO_WHATSAPP_FROM is not available.");
        }
        return from;
    }

    public String getPhoneNumber() {
        String number = phoneNumber != null ? phoneNumber : System.getProperty("TWILIO_PHONE_NUMBER");
        if (number == null) {
            logger.error("TWILIO_PHONE_NUMBER is not available.");
        }
        return number;
    }
}
