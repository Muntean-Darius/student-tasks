package com.example.studenttasks.service;

import com.example.studenttasks.model.dto.request.UserRegistrationRequest;
import com.example.studenttasks.model.dto.response.UserResponse;
import com.example.studenttasks.model.entity.Role;
import com.example.studenttasks.model.entity.User;
import com.example.studenttasks.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_Success() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest("testuser", "password123");
        User savedUser = new User(1L, "testuser", "hashedPassword", Role.USER);

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponse response = userService.registerUser(request);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("USER", response.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UsernameTaken_ThrowsException() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest("testuser", "password123");
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(request);
        });
        assertEquals("Username is already taken!", exception.getMessage());
    }
}