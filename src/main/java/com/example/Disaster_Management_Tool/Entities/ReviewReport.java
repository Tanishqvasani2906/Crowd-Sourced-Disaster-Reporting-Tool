package com.example.Disaster_Management_Tool.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_report")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "affected_people")
    private String affectedPeople;
    @Column(name = "no_of_people_rescued")
    private String NumberOfPeopleRescued;
    @Column(name = "no_of_people_died")
    private String Casualties;
    @Column(name = "detailed_description")
    private String DetailedDescription;
    @Column(name = "evacuation_centres")
    private String EvacuationCentres;
    @Column(name = "approved")
    private Boolean Approved = false;

    @OneToOne
    @JoinColumn(name = "disaster_report_id", referencedColumnName = "id")
    private DisasterReport disasterReport;

}
