package com.example.cunder.repository;

import com.example.cunder.model.User;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);


    @Query(value = "SELECT * FROM users u WHERE u.username=:username",nativeQuery = true)
    Optional<User> findUserByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsById(String id);

    boolean existsByUsername(String username);

    @Query(value = "SELECT CASE WHEN EXISTS (" +
            "SELECT 1 FROM user_roles u WHERE u.user_id=:userId AND u.role_id=:roleId)" +
            "THEN TRUE ELSE FALSE END", nativeQuery = true)
    boolean roleAssignedBefore(String userId, String roleId);

    @Query(value = "SELECT * FROM users u WHERE EXISTS (SELECT user_id FROM user_roles WHERE role_id=:roleId)", nativeQuery = true)
    List<User> getAllUsersByRole(String roleId);

    Page<User> findAll(@Nullable Pageable pageable);

}
