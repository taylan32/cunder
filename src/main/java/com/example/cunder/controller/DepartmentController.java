package com.example.cunder.controller;

import com.example.cunder.dto.department.CreateDepartmentRequest;
import com.example.cunder.model.Department;
import com.example.cunder.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<Void> createDepartment(@RequestBody CreateDepartmentRequest request) {
        departmentService.createDepartment(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getDepartments());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Department> getByCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(departmentService.findDepartmentByCode(code));
    }



}
