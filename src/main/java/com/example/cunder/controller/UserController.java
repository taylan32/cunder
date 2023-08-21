package com.example.cunder.controller;

import com.example.cunder.dto.user.AssignRoleRequest;
import com.example.cunder.dto.user.UserDto;
import com.example.cunder.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/assign")
    public ResponseEntity<Void> assignRole(@RequestBody @Valid AssignRoleRequest request) {
        userService.assignRole(request.userId(), request.roleName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getByRole")
    public ResponseEntity<List<UserDto>> getAllUsersByRole(@RequestParam String role) {
        return ResponseEntity.ok(userService.getAllUserByRole(role));
    }

}
