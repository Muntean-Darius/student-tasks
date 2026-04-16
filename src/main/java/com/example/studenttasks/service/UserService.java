package com.example.studenttasks.service;

import com.example.studenttasks.model.dto.request.UserRegistrationRequest;
import com.example.studenttasks.model.dto.response.UserResponse;
import com.example.studenttasks.model.entity.Role;
import com.example.studenttasks.model.entity.User;
import com.example.studenttasks.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(UserRegistrationRequest request){
        return createUserWithRole(request, Role.USER);
    }

    public UserResponse createAdmin(UserRegistrationRequest request){
        return createUserWithRole(request, Role.ADMIN);
    }

    private UserResponse createUserWithRole(UserRegistrationRequest request, Role role){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(request.getUsername());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(role);

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setRole(savedUser.getRole().name());

        return response;
    }
}
