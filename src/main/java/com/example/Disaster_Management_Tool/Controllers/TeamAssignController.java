package com.example.Disaster_Management_Tool.Controllers;

import com.example.Disaster_Management_Tool.Dto.TeamAssignRequest;
import com.example.Disaster_Management_Tool.Entities.TeamAssign;
import com.example.Disaster_Management_Tool.Entities.User;
import com.example.Disaster_Management_Tool.Services.TeamAssignServices;
import com.example.Disaster_Management_Tool.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team_assign")
public class TeamAssignController {
    @Autowired
    private TeamAssignServices teamAssignServices;
    @Autowired
    private UserService userService;

//    @PostMapping("/addteam")
//    public ResponseEntity<TeamAssign> addTeam(@RequestBody TeamAssignRequest teamAssignRequest, @RequestBody TeamAssign teamAssign) {
//        // Get the current authenticated user
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//
//        // Fetch user entity based on the username
//        User user = userService.findByUsername(username);
//        teamAssign.setUser(user);
//        TeamAssign teamAssign1 = teamAssignServices.addTeam(teamAssign);
//        return ResponseEntity.ok(teamAssign1);
//    }
    @PostMapping("/addteam")
    public ResponseEntity<TeamAssign> addTeam(@RequestBody TeamAssignRequest teamAssignRequest) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        // Now use the request DTO to create the team assignment
        TeamAssign teamAssign = new TeamAssign();
        teamAssign.setTeamName(teamAssignRequest.getTeamName());
        teamAssign.setAssignedBy(user);  // Assuming you want to record who assigned the team

        TeamAssign savedTeam = teamAssignServices.addTeam(teamAssign);

        return ResponseEntity.ok(savedTeam);
    }

    @PutMapping("/unassign/{teamId}")
    public ResponseEntity<TeamAssign> unassignTeam(@PathVariable String teamId) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        TeamAssign updatedTeam = teamAssignServices.unassignTeam(teamId);

        if (updatedTeam == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTeam);
    }




    @GetMapping("/getAllTeams")
    public ResponseEntity<List<TeamAssign>> getAllTeams() {
        List<TeamAssign> teamAssignList = teamAssignServices.addAll();
        return ResponseEntity.ok(teamAssignList);
    }
}
