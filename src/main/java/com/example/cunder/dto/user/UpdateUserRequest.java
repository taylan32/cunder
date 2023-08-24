package com.example.cunder.dto.user;

import com.example.cunder.model.enums.Gender;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateUserRequest(
        @Size(min = 2, max = 15, message = "Name can be between 2 and 15 characters")
        String firstName,

        @Size(min = 2, max = 15, message = "Last name can be between 2 and 15 characters")
        String lastName,
        LocalDate birthDate,

        Gender gender,
        @Size(max = 500, message = "Biography can be at most 500 characters long")
        String bio,
        String departmentId


) {
}
