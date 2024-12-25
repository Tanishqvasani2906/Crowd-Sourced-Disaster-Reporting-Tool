package com.example.Disaster_Management_Tool.Dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisasterReportRequest {
    private String disasterType;
    private String location;
    private String severity;
    private String description;
    private String contactInfo;
    private String imageUrl;
    private String videoUrl;

}
