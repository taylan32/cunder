package com.example.cunder.service;

import com.example.cunder.dto.department.CreateDepartmentRequest;
import com.example.cunder.exception.NotFoundException;
import com.example.cunder.model.Department;
import com.example.cunder.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public void createDepartment(CreateDepartmentRequest request){
        departmentRepository.save(new Department(request.departmentName(), request.code()));
    }
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    public Department findDepartmentById(String id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department Not found"));
    }

    public Department findDepartmentByCode(String code) {
        return departmentRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Department Not found"));
    }

}
