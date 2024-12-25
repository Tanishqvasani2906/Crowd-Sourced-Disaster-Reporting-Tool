package com.example.Disaster_Management_Tool.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOTP(String toEmail, String otp) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(toEmail);
            helper.setSubject("Your OTP Code");
            String htmlContent = "<h1>Your OTP code is: " + otp + "</h1>"
                    + "<p>Please use this code to verify your email.</p>";
            helper.setText(htmlContent, true); // Set true to indicate that the content is HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the error
        }
    }
}
