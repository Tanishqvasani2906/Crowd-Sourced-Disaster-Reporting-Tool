package com.example.Disaster_Management_Tool.Config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    private final Dotenv dotenv;

    // Load the .env file
    public TwilioConfig() {
        this.dotenv = Dotenv.configure().load();
    }

    public String getAccountSid() {
        return dotenv.get("TWILIO_ACCOUNT_SID");
    }

    public String getAuthToken() {
        return dotenv.get("TWILIO_AUTH_TOKEN");
    }

    public String getFromWhatsAppNumber() {
        return dotenv.get("TWILIO_WHATSAPP_FROM");
    }
}
