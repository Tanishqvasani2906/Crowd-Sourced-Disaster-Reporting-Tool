package com.example.Disaster_Management_Tool.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

}
