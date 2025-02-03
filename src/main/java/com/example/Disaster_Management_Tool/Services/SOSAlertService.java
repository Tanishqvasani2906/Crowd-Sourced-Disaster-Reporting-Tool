package com.example.Disaster_Management_Tool.Services;

import com.example.Disaster_Management_Tool.Entities.DisasterReport;
import com.example.Disaster_Management_Tool.Entities.User;
import com.example.Disaster_Management_Tool.Repositories.DisasterReportRepo;
import com.example.Disaster_Management_Tool.Repositories.UserRepo;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SOSAlertService {

    @Autowired
    private DisasterReportRepo disasterReportRepository;

    @Autowired
    private UserRepo userRepository;

    private static final double EARTH_RADIUS_KM = 6371;

    // Loading the Twilio configuration from environment variables
    @Value("${TWILIO_ACCOUNT_SID}")
    private String accountSid;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String authToken;

    @Value("${TWILIO_WHATSAPP_FROM}")
    private String fromWhatsAppNumber;

    // For checking if Twilio credentials are loaded properly
    private void validateTwilioConfig() {
        if (accountSid == null || accountSid.isEmpty()) {
            throw new IllegalStateException("Twilio Account SID is not configured properly.");
        }
        if (authToken == null || authToken.isEmpty()) {
            throw new IllegalStateException("Twilio Auth Token is not configured properly.");
        }
        if (fromWhatsAppNumber == null || fromWhatsAppNumber.isEmpty()) {
            throw new IllegalStateException("Twilio WhatsApp number is not configured properly.");
        }
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    public String sendSOSAlerts(String reportId, double radius, String message) {
        // Validate Twilio configuration
        validateTwilioConfig();

        // Initialize Twilio (make sure the credentials are valid before initializing)
        Twilio.init(accountSid, authToken);

        // Retrieve report details
        DisasterReport report = disasterReportRepository.findByReportId(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        if (report.getLatitude() == null || report.getLongitude() == null) {
            throw new IllegalArgumentException("Report location coordinates not available");
        }

        // Filter users within the specified radius
        List<User> users = userRepository.findAll();
        List<User> usersInArea = users.stream()
                .filter(user -> user.getLatitude() != null && user.getLongitude() != null &&
                        calculateDistance(report.getLatitude(), report.getLongitude(),
                                user.getLatitude(), user.getLongitude()) <= radius)
                .toList();

        if (usersInArea.isEmpty()) {
            return "No users found in the specified area";
        }

        // Send WhatsApp messages to users in the area
        for (User user : usersInArea) {
            sendWhatsAppMessage(user.getPhoneNumber(), report, message);
        }

        return "Alert sent to " + usersInArea.size() + " users in the area";
    }

    private void sendWhatsAppMessage(String to, DisasterReport report, String message) {
        Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber(fromWhatsAppNumber),
                "🚨 *Emergency Alert: Urgent Incident Report* 🚨\n\n" +
                        "\"*⚠\uFE0F An urgent incident has been reported in your area. ⚠\uFE0F* Please stay alert and follow all local authorities' instructions.\n\n" +

                        "*Details of the Incident:*\n" +
                        "📍 *Location:* " + report.getLocation() + "\n" +
                        "🔥 *Type of Disaster:* " + report.getDisasterType() + "\n" +
                        "📝 *Description:* " + report.getDescription() + "\n\n" +

                        "*Message from Authorities:* \n" +
                        message + "\n\n" +

                        "_Please stay alert and follow local authorities' instructions._"
        ).create();
    }

}
