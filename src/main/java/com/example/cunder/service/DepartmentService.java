package com.example.cunder.service;

import com.example.cunder.dto.department.CreateDepartmentRequest;
import com.example.cunder.exception.NotFoundException;
import com.example.cunder.model.Department;
import com.example.cunder.repository.DepartmentRepository;
import com.example.cunder.utils.BasePageableModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public BasePageableModel<Department> getDepartments(int pageNumber, int pageSize, String field, String direction) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize , Sort.by(Sort.Direction.fromString(direction), field));
        Page<Department> departmentPage = departmentRepository.findAll(pageable);
        return new BasePageableModel<>(departmentPage, departmentPage.getContent());
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
