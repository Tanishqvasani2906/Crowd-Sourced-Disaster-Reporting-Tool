package com.example.Disaster_Management_Tool.Dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DisasterReportTeamAssignRequest {
    private String reportId;
    private String teamId;
}
