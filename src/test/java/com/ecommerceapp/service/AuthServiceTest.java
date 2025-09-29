// AuthService Tests
package com.ecommerceapp.service;

import com.ecommerceapp.dto.auth.UserLoginRequest;
import com.ecommerceapp.dto.registration.UserRegisterRequest;
import com.ecommerceapp.model.Role;
import com.ecommerceapp.model.User;
import com.ecommerceapp.repository.UserRepository;
import com.ecommerceapp.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @InjectMocks private AuthService authService;

    @Test
    void testUserRegistration() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(3000L);
            return saved;
        });

        assertDoesNotThrow(() -> authService.register(request));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUserLogin() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encoded");
        user.setRole(Role.USER);
        user.setActive(true);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", "USER")).thenReturn("token");

        String token = authService.login(request);
        assertEquals("token", token);
    }
}