package com.example.cunder.repository;

import com.example.cunder.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    boolean existsByRoleName(String roleName);

    Optional<Role> findByRoleName(String name);
}
