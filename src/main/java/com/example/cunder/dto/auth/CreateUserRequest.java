package com.example.cunder.dto.auth;

import com.example.cunder.model.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserRequest(
        @Email
        @NotBlank(message = "Email is not allowed to be empty")
        String email,
        @NotBlank(message = "Name is not allowed to be empty")
        @Size(min = 2, max = 15, message = "Name can be between 2 and 15 characters")
        String firstName,
        @NotBlank(message = "Name is not allowed to be empty")
        @Size(min = 2, max = 15, message = "Last name can be between 2 and 15 characters")
        String lastName,
        @NotBlank(message = "Username is not allowed to be empty")
        @Size(min = 4, max = 20, message = "Username can be between 4 and 20 characters")
        String username,
        @NotBlank(message = "Password is not allowed to be empty")
        @Size(min = 6, max = 16, message = "Password can be between 6 and 16 characters")
        String password,
        @NotNull(message = "Birth date cannot be null")
        LocalDate birthDate,
        @NotNull(message = "Gender cannot be null")
        Gender gender

) {
}
