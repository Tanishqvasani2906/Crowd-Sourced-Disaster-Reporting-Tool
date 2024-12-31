package com.example.Disaster_Management_Tool.Entities;

import com.example.Disaster_Management_Tool.Dto.DisasterReportStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "disaster_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisasterReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Disaster type is required")
    @Size(max = 100, message = "Disaster type should not exceed 100 characters")
    private String disasterType;

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location should not exceed 255 characters")
    private String location;

//    @NotBlank(message = "Severity is required")
    @NotNull
    @Pattern(regexp = "Emergency|NonEmergency|Critical|LowPriority", message = "Severity must be either 'Emergency', 'Critical', or 'LowPriority'")
    private String severity;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description should not exceed 1000 characters")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @NotBlank(message = "Contact information is required")
    @Size(max = 255, message = "Contact information should not exceed 255 characters")
    @Pattern(regexp = "^[0-9+()\\- ]+$", message = "Contact information contains invalid characters")
    private String contactInfo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imageUrl;

    @Column(name = "report_id", updatable = false, nullable = false, unique = true)
    private String reportId;

    @Temporal(TemporalType.TIMESTAMP)
    @PastOrPresent(message = "Creation date cannot be in the future")
    private Date createdAt = new Date();  // Default to the current date

    @Enumerated(EnumType.STRING)
    private DisasterReportStatus status;

    // Add the many-to-one relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_assign_id")
    private TeamAssign teamAssign;

    @OneToOne(mappedBy = "disasterReport", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private ReviewReport reviewReport;


}
