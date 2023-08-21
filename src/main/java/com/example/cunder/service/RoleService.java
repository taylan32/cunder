package com.example.cunder.service;

import com.example.cunder.dto.role.CreateRoleRequest;
import com.example.cunder.exception.AlreadyExistsException;
import com.example.cunder.exception.NotFoundException;
import com.example.cunder.model.Role;
import com.example.cunder.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void createRole(CreateRoleRequest request) {
        if(roleRepository.existsByRoleName(request.roleName().toUpperCase(Locale.ENGLISH))) {
            throw new AlreadyExistsException(request.roleName().toUpperCase(Locale.ENGLISH) +" role was added earlier");
        }
        roleRepository.save(new Role(request.roleName().toUpperCase(Locale.ENGLISH)));
    }
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    public Role findRoleById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }
    protected Role findRoleByName(String name) {
        return roleRepository.findByRoleName(name)
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }

}
