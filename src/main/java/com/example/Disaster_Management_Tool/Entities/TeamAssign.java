package com.example.Disaster_Management_Tool.Entities;

import com.example.Disaster_Management_Tool.Dto.DisasterReportStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "team_assign")
@Data
public class TeamAssign {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String team_id;
    @Column(name = "team_name")
    private String teamName;

    @Enumerated(EnumType.STRING)
    private Team_status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "teamAssign", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DisasterReport> disasterReports;

    public void setAssignedBy(User user) {
        this.user = user;
    }
}
