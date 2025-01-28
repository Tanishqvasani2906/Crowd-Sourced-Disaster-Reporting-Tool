package com.example.Disaster_Management_Tool.Controllers;

import com.example.Disaster_Management_Tool.Services.SOSAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sos-alerts")
public class SOSAlertController {

    @Autowired
    private SOSAlertService sosAlertService;

    @PostMapping
    public String sendSOSAlert(
            @RequestParam String reportId,
            @RequestParam double radius,
            @RequestParam String message
    ) {
        return sosAlertService.sendSOSAlerts(reportId, radius, message);
    }
}
