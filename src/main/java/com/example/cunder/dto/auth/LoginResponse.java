package com.example.cunder.dto.auth;



public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
