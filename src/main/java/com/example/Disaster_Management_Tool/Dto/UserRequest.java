package com.example.Disaster_Management_Tool.Dto;

import com.example.Disaster_Management_Tool.Entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Min 3 characters are required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Min 6 characters are required")
    private String password;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Role is required")
    private Role role;  // New field for role (USER, ADMIN, MODERATOR, etc.)

    @NotBlank(message = "Location is required")
    private String location;  // New field for location

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90.0 and 90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90.0 and 90.0")
    private Float latitude;  // New field for latitude

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180.0 and 180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180.0 and 180.0")
    private Float longitude;  // New field for longitude
}
