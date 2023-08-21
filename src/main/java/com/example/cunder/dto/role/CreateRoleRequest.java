package com.example.cunder.dto.role;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequest(
        @NotBlank(message = "Role name is not allowed to be empty")
        String roleName
) {
}
