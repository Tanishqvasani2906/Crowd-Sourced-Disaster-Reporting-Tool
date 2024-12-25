package com.example.Disaster_Management_Tool.Repositories;

import com.example.Disaster_Management_Tool.Entities.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReportRepo extends JpaRepository<ReviewReport, Integer> {

}
