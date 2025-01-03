package com.example.Disaster_Management_Tool.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewReportRequest {
    private String affectedPeople;
    private String disasterReportId; // Only the ID is required here
    private Boolean approved;
    private String NumberOfPeopleRescued;
    private String Casualties;
    private String EvacuationCentres;
    private String DetailedDescription;
}
