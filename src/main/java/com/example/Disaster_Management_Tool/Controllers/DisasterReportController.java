package com.example.Disaster_Management_Tool.Controllers;

import com.example.Disaster_Management_Tool.Dto.DisasterReportRequest;
import com.example.Disaster_Management_Tool.Dto.DisasterReportStatus;
import com.example.Disaster_Management_Tool.Dto.DisasterReportTeamAssignRequest;
import com.example.Disaster_Management_Tool.Dto.SearchDisaster;
import com.example.Disaster_Management_Tool.Entities.DisasterReport;
import com.example.Disaster_Management_Tool.Entities.User;
import com.example.Disaster_Management_Tool.Services.DisasterReportServices;

import com.example.Disaster_Management_Tool.Services.UserService;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/disaster-report")
public class DisasterReportController {
    @Autowired
    private DisasterReportServices disasterReportServices;


    @Autowired
    private UserService userServices;

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/submit")
    public ResponseEntity<?> createDisasterReport(@RequestBody DisasterReportRequest disasterReportRequest) {
        System.out.println(disasterReportRequest);
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch user entity based on the username
        User user = userServices.findByUsername(username);

        //create disaster instance
        DisasterReport disasterReport = new DisasterReport();
        disasterReport.setUser(user);
        disasterReport.setDisasterType(disasterReportRequest.getDisasterType());
        disasterReport.setLocation(disasterReportRequest.getLocation());
        disasterReport.setSeverity(disasterReportRequest.getSeverity());
        disasterReport.setContactInfo(disasterReportRequest.getContactInfo());
        disasterReport.setDescription(disasterReportRequest.getDescription());
        disasterReport.setImageUrl(disasterReportRequest.getImageUrl()); // Directly set the image URL
        disasterReport.setVideoUrl(disasterReportRequest.getVideoUrl()); // Directly set the video URL
        disasterReport.setCreatedAt(new Date());
        disasterReport.setStatus(DisasterReportStatus.PENDING); // Set default status


        DisasterReport savedDisasterReport = disasterReportServices.saved(disasterReport);

        return new ResponseEntity<>(savedDisasterReport, HttpStatus.CREATED);
    }
//@PostMapping("/disaster-report")
//public ResponseEntity<?> createDisasterReport(@RequestBody DisasterReport disasterReport) {
//    User currentUser = userServices.getCurrentUser(); // Get current user (from session, JWT, etc.)
//    disasterReport.setUser(currentUser);
//    disasterReportServices.saved(disasterReport);
//    return ResponseEntity.ok("Report submitted");
//}

    @PostMapping("/filter")
    public ResponseEntity<List<DisasterReport>> filterDisasterReports(@RequestBody SearchDisaster searchDisaster){
        List<DisasterReport> reports;

        if (searchDisaster.getDisasterType() != null && searchDisaster.getLocation() != null) {
            reports = disasterReportServices.getReportsByTypeAndLocation(searchDisaster.getDisasterType(), searchDisaster.getLocation());
        } else if (searchDisaster.getDisasterType() != null) {
            reports = disasterReportServices.getReportsByDisasterType(searchDisaster.getDisasterType());
        } else if (searchDisaster.getLocation() != null) {
            reports = disasterReportServices.getReportsByLocation(searchDisaster.getLocation());
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(reports);
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<?> reviewDisasterReport(@PathVariable String id) {
        try {
            DisasterReport disasterReport = disasterReportServices.findById(id);

            if (disasterReport == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Disaster report not found");
            }

            if (disasterReport.getStatus() != DisasterReportStatus.PENDING) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Report is not in PENDING status");
            }

            disasterReport.setStatus(DisasterReportStatus.IN_PROGRESS);
            disasterReportServices.saved(disasterReport);

            return ResponseEntity.ok("Disaster report status updated to IN_PROGRESS");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating report status");
        }
    }
    @GetMapping("/admin-reports")
    public ResponseEntity<List<DisasterReport>> getAllReports(){
        List<DisasterReport> reports = disasterReportServices.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @PutMapping("/completed/{id}")
    public ResponseEntity<?> completedDisasterReport(@PathVariable String id) {
        try {
            DisasterReport disasterReport = disasterReportServices.findById(id);
            if (disasterReport == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Disaster report not found");
            }
            if (disasterReport.getStatus() == DisasterReportStatus.COMPLETED) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Report is not in COMPLETED status");
            }
            disasterReport.setStatus(DisasterReportStatus.COMPLETED);
            disasterReportServices.saved(disasterReport);
            return ResponseEntity.ok("Disaster report status updated to COMPLETED");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating report status");
        }
    }

    @GetMapping("/user-reports")
    public ResponseEntity<?> getReportsForUser() {
        try {
            // Fetch user by userId
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


            // Get the username of the currently authenticated user
            String username = null;
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                    System.out.println("Username : " + username);
                } else {
                    username = principal.toString();
                }
            }

            User user = userServices.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Find all reports submitted by the user
            List<DisasterReport> userReports = disasterReportServices.findByuserId(user.getId());

            return ResponseEntity.ok(userReports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching reports");
        }
    }
    @PutMapping("/assign-team")
    public ResponseEntity<String> teamAssignRequest(@RequestBody DisasterReportTeamAssignRequest request){
        disasterReportServices.assignTeamToReport(request.getReportId(), request.getTeamId());
        return ResponseEntity.ok("Team assigned to report successfully");
    }
}
