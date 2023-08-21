package com.example.cunder.dto.user;

import jakarta.validation.constraints.NotBlank;

public record AssignRoleRequest(
        @NotBlank(message = "User is not allowed to be empty")
        String userId,
        @NotBlank(message = "Role is not allowed to be empty")
        String roleName
) {
}
