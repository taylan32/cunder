package com.example.cunder.controller;

import com.example.cunder.dto.role.CreateRoleRequest;
import com.example.cunder.model.Role;
import com.example.cunder.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Void> createRole(@RequestBody @Valid CreateRoleRequest request) {
        roleService.createRole(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findRoleById(@PathVariable("id") String id) {
        return ResponseEntity.ok(roleService.findRoleById(id));
    }

}
