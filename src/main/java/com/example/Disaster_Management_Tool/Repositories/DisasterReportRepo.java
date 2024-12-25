package com.example.Disaster_Management_Tool.Repositories;

import com.example.Disaster_Management_Tool.Entities.DisasterReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisasterReportRepo extends JpaRepository<DisasterReport, String> {

    List<DisasterReport> findByDisasterType(String disasterType);

    List<DisasterReport> findByLocation(String location);

    List<DisasterReport> findByDisasterTypeAndLocation(String disasterType, String location);

    List<DisasterReport> findByUserId(String userId);
}
