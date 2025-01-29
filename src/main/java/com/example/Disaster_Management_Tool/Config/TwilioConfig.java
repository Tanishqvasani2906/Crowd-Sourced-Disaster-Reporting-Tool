package com.example.Disaster_Management_Tool.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromWhatsAppNumber;

//    @Value("${twilio.sms.auth.token}")
//    private String smsAuthToken;
//
//    public String getSmsAuthToken() {
//        return smsAuthToken;
//    }
//
//    public void setSmsAuthToken(String smsAuthToken) {
//        this.smsAuthToken = smsAuthToken;
//    }

    public String getAccountSid() {
        return accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getFromWhatsAppNumber() {
        return fromWhatsAppNumber;
    }
}
