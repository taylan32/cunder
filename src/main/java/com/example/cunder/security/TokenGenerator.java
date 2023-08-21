package com.example.cunder.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenGenerator {

    @Value("${application.access_token_key}")
    private String KEY;

    @Value("${application.issuer}")
    private String ISSUER;

    @Value("${application.access_token_expiration}")
    private Long EXPIRES_ACCESS_TOKEN_MINUTE;

    public String generateToken(Authentication authentication) {
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        List<String> roles = new ArrayList<>();
        authentication.getAuthorities().forEach(role -> {
            roles.add(role.getAuthority());
        });

        return JWT.create()
                .withSubject(username)
                .withClaim("role",authentication.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList()))
                .withExpiresAt(new Date(System.currentTimeMillis() + (EXPIRES_ACCESS_TOKEN_MINUTE * 600 * 1000)))
                .sign(Algorithm.HMAC256(KEY.getBytes()));
    }

    public DecodedJWT verifyJWT(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(KEY.getBytes())).build();
        try {
            DecodedJWT decodedToken = verifier.verify(token);
            return decodedToken;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
