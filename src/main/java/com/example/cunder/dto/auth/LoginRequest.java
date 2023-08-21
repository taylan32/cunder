package com.example.cunder.dto.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Username is not allowed to be empty")
        @Size(min = 4, max = 20, message = "Username can be between 4 and 20 characters")
        String username,
        @NotBlank(message = "Password is not allowed to be empty")
        @Size(min = 6, max = 16, message = "Password can be between 6 and 16 characters")
        String password
) {
}
