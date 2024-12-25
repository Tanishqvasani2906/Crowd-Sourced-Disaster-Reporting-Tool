package com.example.Disaster_Management_Tool.Services;

import com.example.Disaster_Management_Tool.Entities.TeamAssign;
import com.example.Disaster_Management_Tool.Entities.Team_status;
import com.example.Disaster_Management_Tool.Repositories.TeamAssignRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamAssignServices {

    @Autowired
    private TeamAssignRepo teamAssignRepo;
    public TeamAssign addTeam(TeamAssign teamAssignRequest) {
        if (teamAssignRequest.getStatus() == null) {
            teamAssignRequest.setStatus(Team_status.IDEAL);  // Set default status to IDEAL if not provided
        }
        return teamAssignRepo.save(teamAssignRequest);
    }

    public List<TeamAssign> addAll() {
        return teamAssignRepo.findAll();
    }

    public TeamAssign unassignTeam(String teamId) {
        // Fetch the team assignment by ID
        Optional<TeamAssign> optionalTeamAssign = teamAssignRepo.findById(teamId);
        if (optionalTeamAssign.isPresent()) {
            TeamAssign teamAssign = optionalTeamAssign.get();

            // Set the status to 'IDEAL' using the enum
            teamAssign.setStatus(Team_status.IDEAL);

            // Save the updated entity
            return teamAssignRepo.save(teamAssign);
        }
        return null; // Team not found
    }
}
