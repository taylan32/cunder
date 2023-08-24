package com.example.cunder.service;

import com.example.cunder.dto.user.UserDto;
import com.example.cunder.exception.AlreadyExistsException;
import com.example.cunder.exception.NotFoundException;
import com.example.cunder.model.Role;
import com.example.cunder.model.User;
import com.example.cunder.model.enums.MembershipType;
import com.example.cunder.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
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

    protected boolean existsByEmail(String email ){
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
        if(userRepository.roleAssignedBefore(userId, role.getId())) {
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
                user.getBirthOfDate(),
                user.getGender(),
                user.getProfileImage(),
                user.getProfileImage(),
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
        if(user.getMembershipType().equals(membershipType)) {
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
                user.getBirthOfDate(),
                user.getGender(),
                user.getProfileImage(),
                user.getProfileImage(),
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

}
