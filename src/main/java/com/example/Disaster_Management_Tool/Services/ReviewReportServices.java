package com.example.Disaster_Management_Tool.Services;

import com.example.Disaster_Management_Tool.Dto.ReviewReportRequest;
import com.example.Disaster_Management_Tool.Entities.DisasterReport;
import com.example.Disaster_Management_Tool.Entities.ReviewReport;
import com.example.Disaster_Management_Tool.Repositories.DisasterReportRepo;
import com.example.Disaster_Management_Tool.Repositories.ReviewReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ReviewReportServices {
    @Autowired
    private ReviewReportRepo reviewReportRepo;
    @Autowired
    private DisasterReportRepo disasterReportRepo;

    public ReviewReport createReviewReport(ReviewReportRequest request) {
        // Fetch the associated DisasterReport entity
        Optional<DisasterReport> disasterReportOpt = disasterReportRepo.findById(request.getDisasterReportId());
        if (disasterReportOpt.isEmpty()) {
            throw new IllegalArgumentException("Disaster report not found with id: " + request.getDisasterReportId());
        }

        DisasterReport disasterReport = disasterReportOpt.get();

        // Map DTO to Entity
        ReviewReport reviewReport = new ReviewReport();
        reviewReport.setAffectedPeople(request.getAffectedPeople());
        reviewReport.setNumberOfPeopleRescued(request.getNumberOfPeopleRescued());
        reviewReport.setCasualties(request.getCasualties());
        reviewReport.setEvacuationCentres(request.getEvacuationCentres());
        reviewReport.setDetailedDescription(request.getDetailedDescription());
        reviewReport.setApproved(request.getApproved());
        reviewReport.setDisasterReport(disasterReport);

        // Save and return
        return reviewReportRepo.save(reviewReport);
    }

}
