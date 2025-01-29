package com.example.Disaster_Management_Tool.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // Auto-generated UUID

    @Column(nullable = false, unique = true)
    private String username; // Mandatory

    @Column(nullable = false, unique = true)
    private String phoneNumber; // Mandatory and unique

    @Column(nullable = true)
    private String email; // Optional (nullable)

    @Column(nullable = true)
    private String password; // Optional (nullable)

    @Column(nullable = false)
    private String location; // Mandatory

    @Column(nullable = false)
    private Float latitude; // Mandatory

    @Column(nullable = false)
    private Float longitude; // Mandatory

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // Mandatory, default to USER if not provided

    @Column(nullable = true)
    private LocalDateTime createdAt; // Optional

    @Column(nullable = true)
    private LocalDateTime updatedAt; // Optional
}
