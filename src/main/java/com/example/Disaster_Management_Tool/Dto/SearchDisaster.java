package com.example.Disaster_Management_Tool.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDisaster {

    @Column(nullable = true)
    private String disasterType;

    @Column(nullable = true)
    private String location;
}
