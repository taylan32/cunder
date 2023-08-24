package com.example.cunder.service;

import com.example.cunder.dto.user.UpdateUserRequest;
import com.example.cunder.dto.user.UserDto;
import com.example.cunder.exception.AlreadyExistsException;
import com.example.cunder.exception.NotFoundException;
import com.example.cunder.model.Role;
import com.example.cunder.model.User;
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

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final DepartmentService  departmentService;
    public UserService(UserRepository userRepository,
                       RoleService roleService,
                       DepartmentService departmentService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.departmentService = departmentService;
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

    protected boolean existsById(String id) {
        return userRepository.existsById(id);
    }

    public void assignRole(String userId, String roleName) {
        Role role = roleService.findRoleByName(roleName.toUpperCase(Locale.ENGLISH));
        User user = findUserById(userId);
        if (userRepository.roleAssignedBefore(userId, role.getId())) {
            throw new AlreadyExistsException("Role has already been assigned to this user");
        }

        Set<Role> roles = user.getRoles();
        roles.add(role);
        User newUser = new User(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getDepartment(),
                user.getUsername(),
                user.getPassword(),
                user.getBirthDate(),
                user.getGender(),
                user.getProfileImage(),
                user.getCoverImage(),
                user.getBio(),
                user.isDeleted(),
                user.isVerified(),
                user.isBanned(),
                user.getMembershipType(),
                roles
        );
        user.setCreatedAt(user.getCreatedAt());
        userRepository.save(newUser);

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
        User updatedUser = new User(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getDepartment(),
                user.getUsername(),
                user.getPassword(),
                user.getBirthDate(),
                user.getGender(),
                user.getProfileImage(),
                user.getCoverImage(),
                user.getBio(),
                user.isDeleted(),
                user.isVerified(),
                user.isBanned(),
                membershipType,
                user.getRoles()
        );
        updatedUser.setCreatedAt(user.getCreatedAt());
        userRepository.save(updatedUser);
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
        User user = findByUsername(username);
        if(user == null) {
            throw new NotFoundException("User not found");
        }
        User updatedUser = null;
        if(checkIfAuthenticatedUserHasRole("ADMIN")) {
             updatedUser = new User(
                     user.getId(),
                     user.getEmail(),
                     request.firstName() == null ? user.getFirstName() : request.firstName(),
                     request.lastName() == null ? user.getLastName() : request.lastName(),
                     request.departmentId() == null ? user.getDepartment() : departmentService.findDepartmentById(request.departmentId()),
                     user.getUsername(),
                     user.getPassword(),
                     request.birthDate() == null ? user.getBirthDate() : request.birthDate(),
                     request.gender() == null ? user.getGender() : request.gender(),
                     user.getProfileImage(),
                     user.getCoverImage(),
                     request.bio() == null ? user.getBio() : request.bio(),
                     user.isDeleted(),
                     user.isVerified(),
                     user.isBanned(),
                     user.getMembershipType(),
                     user.getRoles()
             );
        }
        else {
             updatedUser = new User(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getDepartment(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getBirthDate(),
                    user.getGender(),
                    user.getProfileImage(),
                    user.getCoverImage(),
                    request.bio() == null ? user.getBio() : request.bio(),
                    user.isDeleted(),
                    user.isVerified(),
                    user.isBanned(),
                    user.getMembershipType(),
                    user.getRoles()
            );
        }
        updatedUser.setCreatedAt(user.getCreatedAt());
        userRepository.save(updatedUser);
    }

    private boolean checkIfAuthenticatedUserHasRole(String roleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getAuthorities().stream().map(role-> role.getAuthority()).collect(Collectors.toList()).contains(roleName) == true)   {
            return true;
        }
        return false;
    }

}
