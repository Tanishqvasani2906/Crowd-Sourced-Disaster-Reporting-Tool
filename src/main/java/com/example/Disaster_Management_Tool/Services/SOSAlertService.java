package com.example.Disaster_Management_Tool.Services;

import com.example.Disaster_Management_Tool.Entities.DisasterReport;
import com.example.Disaster_Management_Tool.Entities.User;
import com.example.Disaster_Management_Tool.Repositories.DisasterReportRepo;
import com.example.Disaster_Management_Tool.Repositories.UserRepo;
import io.github.cdimascio.dotenv.Dotenv;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SOSAlertService {

    @Autowired
    private DisasterReportRepo disasterReportRepository;

    @Autowired
    private UserRepo userRepository;

    private final Dotenv dotenv;

    private static final double EARTH_RADIUS_KM = 6371;

    public SOSAlertService() {
        this.dotenv = Dotenv.configure().load(); // Load .env file
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
        // Retrieve Twilio credentials and phone number from the .env file
        String accountSid = dotenv.get("TWILIO_ACCOUNT_SID");
        String authToken = dotenv.get("TWILIO_AUTH_TOKEN");
        String fromWhatsAppNumber = dotenv.get("TWILIO_WHATSAPP_FROM");

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

        // Initialize Twilio with credentials from the .env file
        Twilio.init(accountSid, authToken);

        // Send WhatsApp messages to users in the area
        for (User user : usersInArea) {
            sendWhatsAppMessage(user.getPhoneNumber(), report, message, fromWhatsAppNumber);
        }

        return "Alert sent to " + usersInArea.size() + " users in the area";
    }

    private void sendWhatsAppMessage(String to, DisasterReport report, String message, String fromWhatsAppNumber) {
        Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber(fromWhatsAppNumber),
                "Emergency Alert: An incident has been reported in your area.\n\n" +
                        "Location: " + report.getLocation() + "\n" +
                        "Type: " + report.getDisasterType() + "\n" +
                        "Description: " + report.getDescription() + "\n\n" +
                        message +
                        "\n\nPlease stay alert and follow local authorities' instructions."
        ).create();
    }
}
