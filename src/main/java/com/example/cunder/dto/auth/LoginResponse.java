package com.example.cunder.dto.auth;

import com.example.cunder.dto.user.UserDto;

public record LoginResponse(
        String accessToken,
        UserDto user
) {
}
