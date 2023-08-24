package com.example.cunder.controller;

import com.example.cunder.dto.department.CreateDepartmentRequest;
import com.example.cunder.model.Department;
import com.example.cunder.service.DepartmentService;
import com.example.cunder.utils.BasePageableModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> createDepartment(@RequestBody CreateDepartmentRequest request) {
        departmentService.createDepartment(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<BasePageableModel<Department>> getAllDepartments(@RequestParam(defaultValue = "1",required = false) int pageNumber,
                                                                           @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                                           @RequestParam(defaultValue = "code",required = false) String field,
                                                                           @RequestParam(defaultValue = "asc", required = false) String direction) {
        return ResponseEntity.ok(departmentService.getDepartments(pageNumber, pageSize, field, direction));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Department> getByCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(departmentService.findDepartmentByCode(code));
    }



}
