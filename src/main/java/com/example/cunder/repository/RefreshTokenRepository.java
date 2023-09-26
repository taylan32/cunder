package com.example.cunder.repository;

import com.example.cunder.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {


    @Query(value = "SELECT rf.* FROM users u INNER JOIN refresh_tokens rf ON u.id=rf.user_id WHERE u.username=:username",nativeQuery = true)
    RefreshToken getByUsername(String username);
}
