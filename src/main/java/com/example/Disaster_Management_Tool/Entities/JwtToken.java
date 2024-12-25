package com.example.Disaster_Management_Tool.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String token;

    private String userId;

    public JwtToken(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }
}
