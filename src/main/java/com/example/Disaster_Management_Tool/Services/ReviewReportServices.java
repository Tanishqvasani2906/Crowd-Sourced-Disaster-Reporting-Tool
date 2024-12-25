package com.example.Disaster_Management_Tool.Services;

import com.example.Disaster_Management_Tool.Entities.ReviewReport;
import com.example.Disaster_Management_Tool.Repositories.ReviewReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ReviewReportServices {
    @Autowired
    private ReviewReportRepo reviewReportRepo;
    public ReviewReport saveReviewReport(ReviewReport newReviewReport) {
        return reviewReportRepo.save(newReviewReport);
    }

}
