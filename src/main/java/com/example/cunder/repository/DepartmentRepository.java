package com.example.cunder.repository;

import com.example.cunder.model.Department;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, String> {

    Optional<Department> findByCode(String code);

    Page<Department> findAll(@Nullable Pageable pageable);
}
