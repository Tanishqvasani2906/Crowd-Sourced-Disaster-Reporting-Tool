package com.example.Disaster_Management_Tool.Repositories;

import com.example.Disaster_Management_Tool.Entities.TeamAssign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamAssignRepo extends JpaRepository<TeamAssign, String> {

}
