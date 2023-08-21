package com.example.cunder.repository;

import com.example.cunder.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, String> {

    Optional<Department> findByCode(String code);
}
