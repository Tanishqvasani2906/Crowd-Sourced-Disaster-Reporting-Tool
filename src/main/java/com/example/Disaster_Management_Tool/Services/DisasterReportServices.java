package com.example.Disaster_Management_Tool.Services;

import com.example.Disaster_Management_Tool.Entities.DisasterReport;
import com.example.Disaster_Management_Tool.Entities.TeamAssign;
import com.example.Disaster_Management_Tool.Entities.Team_status;
import com.example.Disaster_Management_Tool.Repositories.DisasterReportRepo;
import com.example.Disaster_Management_Tool.Repositories.TeamAssignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisasterReportServices {
    @Autowired
    private DisasterReportRepo disasterReportRepo;
    @Autowired
    private TeamAssignRepo teamAssignRepo;

    public DisasterReport saved(DisasterReport disasterReport) {
         disasterReportRepo.save(disasterReport);
        return disasterReport;
    }
    public List<DisasterReport> getReportsByDisasterType(String disasterType) {
        return disasterReportRepo.findByDisasterType(disasterType);
    }

    // Fetch all reports by location
    public List<DisasterReport> getReportsByLocation(String location) {
        return disasterReportRepo.findByLocation(location);
    }

    // Fetch reports by both disaster type and location
    public List<DisasterReport> getReportsByTypeAndLocation(String disasterType, String location) {
        return disasterReportRepo.findByDisasterTypeAndLocation(disasterType, location);
    }

    public DisasterReport findById(String id) {
        return disasterReportRepo.findById(String.valueOf(id)).orElse(null);
    }

    public List<DisasterReport> findByuserId(String userId) {
        return disasterReportRepo.findByUserId(userId);
    }

    public List<DisasterReport> getAllReports() {
        return disasterReportRepo.findAll();
    }

    public void assignTeamToReport(String reportId, String teamId) {
        // Fetch the disaster report
        DisasterReport disasterReport = disasterReportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Disaster Report not found"));

        // Fetch the team
        TeamAssign teamAssign = teamAssignRepo.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Assign the team to the report
        disasterReport.setTeamAssign(teamAssign);
        // Change the team status to BUSY if it was IDEAL
        if (teamAssign.getStatus() == Team_status.IDEAL) {
            teamAssign.setStatus(Team_status.BUSY);
        }


        // Save the updated report
        disasterReportRepo.save(disasterReport);
        teamAssignRepo.save(teamAssign);
    }

    public void updateTeamStatus(TeamAssign assignedTeam) {
        return;
    }

    // Service method to find disaster report by reportId
    public Optional<DisasterReport> getDisasterReportByReportId(String reportId) {
        return disasterReportRepo.findByReportId(reportId);
    }

//    public List<DisasterReport> getReportsByUserId(String userId) {
//
//    }
}
