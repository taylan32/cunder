package com.example.cunder.dto.user;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "Password is not allowed to be empty")
        String password,
        @NotBlank(message = "Password repeat is not allowed to be empty")
        String newPassword,
        @NotBlank(message = "New password confirm is not allowed to be empty")
        String newPasswordConfirm
) {
}
