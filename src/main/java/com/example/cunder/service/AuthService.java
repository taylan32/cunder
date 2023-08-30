package com.example.cunder.service;

import com.example.cunder.dto.auth.CreateUserRequest;
import com.example.cunder.dto.auth.LoginRequest;
import com.example.cunder.dto.auth.LoginResponse;
import com.example.cunder.dto.user.ChangePasswordRequest;
import com.example.cunder.dto.user.UserDto;
import com.example.cunder.exception.AlreadyExistsException;
import com.example.cunder.exception.BannedUserLoginException;
import com.example.cunder.exception.NotFoundException;
import com.example.cunder.exception.UnverifiedUserLoginException;
import com.example.cunder.model.Role;
import com.example.cunder.model.User;
import com.example.cunder.model.enums.MembershipType;
import com.example.cunder.security.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentService departmentService;
    private final TokenGenerator tokenGenerator;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder,
                       DepartmentService departmentService,
                       TokenGenerator tokenGenerator,
                       AuthenticationManager authenticationManager,
                       RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.departmentService = departmentService;
        this.tokenGenerator = tokenGenerator;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
    }


    public void register(CreateUserRequest request) {
        checkForRegisterRules(request);
        Role role = roleService.findRoleByName("USER");
        User user = new User(
                request.email(),
                request.firstName(),
                request.lastName(),
                departmentService.findDepartmentByCode("555"),
                request.username(),
                passwordEncoder.encode(request.password()),
                request.birthDate(),
                request.gender(),
                "default.png",
                "default.png",
                "",
                false,
                true, // TODO: email ile doğrulama yaptıktan sonra false olarak değiştir
                false,
                MembershipType.STANDARD,
                new HashSet<>(Set.of(role))
        );
        User registeredUser = userService.createUser(user);
        // send email to verification
        logger.info("User created: " + user.getUsername());
    }

    public LoginResponse login(LoginRequest request) {
        //try {
        checkForLoginRules(request);
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            return new LoginResponse(tokenGenerator.generateToken(auth),
                    UserDto.convert(userService.findByUsername(request.username()))
            );

        /*} catch (final BadCredentialsException exception) {
            throw new BadCredentialsException(exception.getMessage());
        }*/
    }

    private void checkForRegisterRules(CreateUserRequest request) {
        if(userService.existsByEmail(request.email())) {
            logger.error("User could not created. " + request.email() + " is already in use");
            throw new AlreadyExistsException("Email already in use");
        }
        if(userService.existsByUsername(request.username())) {
            logger.error("User could not created. " + request.username() + " is already in use");
            throw new AlreadyExistsException("Username already in use");
        }
    }

    private void checkForLoginRules(LoginRequest request) {
        User user = userService.findByUsername(request.username());
        if(user == null) {
            throw new BadCredentialsException("Bad credentials");
        }
        if(user.isBanned()) {
            logger.error("Banned user try to login: " + user.getUsername());
            throw new BannedUserLoginException("Your account has been banned");
        }
        if(!user.isVerified()) {
            logger.error("Unverified user try to login: " + user.getUsername());
            throw new UnverifiedUserLoginException("Your account has not been verified");
        }
        if(user.isDeleted()) {
            logger.error("Deleted user try to login: " + user.getUsername());
            throw new NotFoundException("User not found");
        }
    }



}
