package com.example.Disaster_Management_Tool.Repositories;

import com.example.Disaster_Management_Tool.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,String> {
    User findByEmail(String email);

    // Find a user by their username, if applicable
    User findByUsername(String username);

    Optional<Object> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
}
