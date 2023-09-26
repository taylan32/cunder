package com.example.cunder.service;

import com.example.cunder.model.RefreshToken;
import com.example.cunder.model.User;
import com.example.cunder.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }


    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository.save(new RefreshToken(user,
                UUID.randomUUID().toString(),
                LocalDate.now().plusDays(7),
                LocalDate.now()));
        return token;
    }

    protected String getRefreshToken(String username) {
        RefreshToken refreshToken = refreshTokenRepository.getByUsername(username);
        if (refreshToken == null || refreshToken.getExpireAt().isBefore(LocalDate.now())) {
            if (refreshToken == null) {
                RefreshToken createdToken = createRefreshToken(userService.findByUsername(username));
                return createdToken.getToken();
            }
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setCreatedAt(LocalDate.now());
            refreshToken.setExpireAt(LocalDate.now().plusDays(7));
            refreshTokenRepository.save(refreshToken);
            return refreshToken.getToken();
        }
        return refreshToken.getToken();
    }


}
