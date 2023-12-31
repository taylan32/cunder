package com.example.cunder.service;

import com.example.cunder.dto.user.ChangePasswordRequest;
import com.example.cunder.dto.user.UpdateUserRequest;
import com.example.cunder.dto.user.UserDto;
import com.example.cunder.exception.AlreadyExistsException;
import com.example.cunder.exception.InvalidPasswordChangeRequestException;
import com.example.cunder.exception.NotFoundException;
import com.example.cunder.model.Role;
import com.example.cunder.model.User;
import com.example.cunder.model.enums.Gender;
import com.example.cunder.model.enums.MembershipType;
import com.example.cunder.repository.UserRepository;
import com.example.cunder.utils.BasePageableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleService roleService,
                       DepartmentService departmentService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.departmentService = departmentService;
        this.passwordEncoder = passwordEncoder;
    }

    protected User createUser(User user) {
        return userRepository.save(user);
    }

    protected User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    protected User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    protected boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    protected boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void assignRole(String userId, String roleName) {
        Role role = roleService.findRoleByName(roleName.toUpperCase(Locale.ENGLISH));
        User user = findUserById(userId);
        /*if (userRepository.roleAssignedBefore(userId, role.getId())) {
            throw new AlreadyExistsException("Role has already been assigned to this user");
        }*/
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);

    }

    public List<UserDto> getAllUserByRole(String roleName) {
        Role role = roleService.findRoleByName(roleName.toUpperCase(Locale.ENGLISH));
        return userRepository.getAllUsersByRole(role.getId())
                .stream()
                .map(UserDto::convert)
                .collect(Collectors.toList());
    }

    public void changeMembershipType(String userId, MembershipType membershipType) {
        User user = findUserById(userId);
        if (user.getMembershipType().equals(membershipType)) {
            throw new AlreadyExistsException("User have already this membership type");
        }
        user.setMembershipType(membershipType);
        userRepository.save(user);
        logger.info("Membership type changed to " + membershipType.toString());
    }

    public BasePageableModel<UserDto> getAllUsers(int pageNumber, int pageSize, String field, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), field);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<User> userPage = userRepository.findAll(pageable);
        return new BasePageableModel<>(userPage,
                userPage
                        .getContent()
                        .stream()
                        .map(UserDto::convert)
                        .collect(Collectors.toList()));
    }

    public void updateUser(String username, UpdateUserRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        setFields(user, request);
        userRepository.save(user);
    }

    private User setFields(User user, UpdateUserRequest request) {

        if (checkIfAuthenticatedUserHasRole("ADMIN")) {
            user.setFirstName(request.firstName() == null ? user.getFirstName() : request.firstName());
            user.setLastName(request.lastName() == null ? user.getLastName() : request.lastName());
            user.setDepartment(request.departmentId() == null ? user.getDepartment() : departmentService.findDepartmentById(request.departmentId()));
            user.setBirthDate(request.birthDate() == null ? user.getBirthDate() : request.birthDate());
            user.setGender(request.gender() == null ? user.getGender() : request.gender());
            user.setBio(request.bio() == null ? user.getBio() : request.bio());
        } else {
            user.setBio(request.bio() == null ? user.getBio() : request.bio());
        }

        return user;
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!user.getPassword().equals(passwordEncoder.encode(request.password()))) {
            throw new InvalidPasswordChangeRequestException("Old password is not matched");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private boolean checkIfAuthenticatedUserHasRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList()).contains(roleName) == true) {
            return true;
        }
        return false;
    }

    protected Set<User> getSuggestion(String gender, int suggestionCount) {
        return userRepository.findRandomUsersWithOppositeGender(gender, suggestionCount);
    }




}
