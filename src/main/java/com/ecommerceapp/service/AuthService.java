package com.ecommerceapp.service;

import com.ecommerceapp.annotation.LogMethod;
import com.ecommerceapp.dto.auth.UserLoginRequest;
import com.ecommerceapp.dto.registration.UserRegisterRequest;
import com.ecommerceapp.dto.registration.UserResponse;
import com.ecommerceapp.exception.AuthException;
import com.ecommerceapp.exception.BusinessException;
import com.ecommerceapp.exception.ResourceNotFoundException;
import com.ecommerceapp.model.Role;
import com.ecommerceapp.model.User;
import com.ecommerceapp.repository.UserRepository;
import com.ecommerceapp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @LogMethod("User registration")
    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setActive(user.isActive());
        return response;
    }

    @LogMethod("User login")
    public String login(UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid credentials");
        }

        if (!user.isActive()) {
            throw new AuthException("User account is deactivated");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
    }
}