package com.example.cunder.controller;

import com.example.cunder.controller.constraint.ChangePasswordRequestConstraint;
import com.example.cunder.dto.user.*;
import com.example.cunder.model.enums.MembershipType;
import com.example.cunder.service.UserService;
import com.example.cunder.utils.BasePageableModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> assignRole(@RequestBody @Valid AssignRoleRequest request) {
        userService.assignRole(request.userId(), request.roleName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getByRole")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsersByRole(@RequestParam String role) {
        return ResponseEntity.ok(userService.getAllUserByRole(role));
    }

    @PatchMapping("/membership-type/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> changeMembershipType(@PathVariable("id") String userId, @RequestBody @Valid ChangeMembershipTypeRequest request) {
        userService.changeMembershipType(userId, request.membershipType());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<BasePageableModel<UserDto>> getAllUsers(@RequestParam(defaultValue = "1",required = false) int pageNumber,
                                                                  @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                                  @RequestParam(defaultValue = "id",required = false) String field,
                                                                  @RequestParam(defaultValue = "asc", required = false) String direction) {

        return ResponseEntity.ok(userService.getAllUsers(pageNumber, pageSize, field, direction));
    }

    @PutMapping("/update/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN') or #username==authentication.principal.username")
    public ResponseEntity<Void> updateUser(@PathVariable("username") String username, @RequestBody @Valid UpdateUserRequest request) {
        userService.updateUser(username, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change-password/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN') or #username==authentication.principal.username")
    public ResponseEntity<Void> changePassword(@PathVariable("username") String username,
                                               @RequestBody @Valid @ChangePasswordRequestConstraint ChangePasswordRequest request){

        userService.changePassword(username, request);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/test")
    public ResponseEntity<Void> getSuggestions() {
        //userService.getSuggestion();
        return ResponseEntity.noContent().build();
    }

}
