package com.example.Disaster_Management_Tool.Controllers;

import com.example.Disaster_Management_Tool.Entities.*;
import com.example.Disaster_Management_Tool.Services.DisasterReportServices;
import com.example.Disaster_Management_Tool.Services.ReviewReportServices;
import com.example.Disaster_Management_Tool.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/review-report")
public class ReviewReportController {
    @Autowired
    private ReviewReportServices reviewReportServices;
    @Autowired
    private UserService userService;
    @Autowired
    private DisasterReportServices disasterReportServices;

    @PostMapping("/addReviewReport")
    public ResponseEntity<ReviewReport> addReviewReport(@RequestBody ReviewReport reviewReport) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        ReviewReport newReviewReport = new ReviewReport();
        newReviewReport.setNumberOfPeopleRescued(reviewReport.getNumberOfPeopleRescued());
        newReviewReport.setCasualties(reviewReport.getCasualties());
        newReviewReport.setAffectedPeople(reviewReport.getAffectedPeople());
        newReviewReport.setEvacuationCentres(reviewReport.getEvacuationCentres());
        newReviewReport.setDetailedDescription(reviewReport.getDetailedDescription());
        // Set approved to true when the review report is submitted
        newReviewReport.setApproved(true); // Set approved to true

        // Fetch the disaster report based on ID and set it in the review report
        if (reviewReport.getDisasterReport() != null && reviewReport.getDisasterReport().getId() != null) {
            DisasterReport disasterReport = disasterReportServices.findById(reviewReport.getDisasterReport().getId());
            newReviewReport.setDisasterReport(disasterReport);  // Set the disaster report
        }

        // Save the review report using the service
        ReviewReport savedReviewReport = reviewReportServices.saveReviewReport(newReviewReport);

        // Return the saved review report as a response
        return ResponseEntity.ok(savedReviewReport);
    }





}
