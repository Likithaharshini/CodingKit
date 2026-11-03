package com.codingkit.service.impl;

import com.codingkit.dto.RegisterRequest;
import com.codingkit.entity.User;
import com.codingkit.exception.ResourceNotFoundException;
import com.codingkit.exception.UnauthorizedAccessException;
import com.codingkit.exception.UserAlreadyExistsException;
import com.codingkit.repository.UserRepository;
import com.codingkit.security.UserPrincipal;
import com.codingkit.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        String fullName = request.getFullName() == null ? "" : request.getFullName().trim();
        String email = request.getEmail() == null ? "" : request.getEmail().trim();
        String username = request.getUsername() == null ? "" : request.getUsername().trim();
        String password = request.getPassword();
        String confirm = request.getConfirmPassword();

        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password == null || confirm == null) {
            throw new IllegalArgumentException("All fields are required.");
        }

        if (!Objects.equals(password, confirm)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username is already in use.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email is already in use.");
        }

        User user = new User(fullName, username, email, passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("User is not authenticated.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userRepository.findById(userPrincipal.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        }
        throw new UnauthorizedAccessException("Invalid user session.");
    }

    @Override
    public User findByUsernameOrEmail(String value) {
        return userRepository.findByUsernameOrEmail(value)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public User updateProfile(Long userId, String fullName, String username, String email) {
        User currentUser = getCurrentUser();
        if (!currentUser.getId().equals(userId)) {
            throw new UnauthorizedAccessException("You are not allowed to update this profile.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!user.getUsername().equals(username) && userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists.");
        }

        user.setFullName(fullName.trim());
        user.setUsername(username.trim());
        user.setEmail(email.trim());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword, String confirmPassword) {
        User currentUser = getCurrentUser();
        if (!currentUser.getId().equals(userId)) {
            throw new UnauthorizedAccessException("You are not allowed to change this password.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        if (!Objects.equals(newPassword, confirmPassword)) {
            throw new IllegalArgumentException("New passwords do not match.");
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean isDuplicateUsername(String username) {
        return username != null && userRepository.existsByUsername(username.trim());
    }

    @Override
    public boolean isDuplicateEmail(String email) {
        return email != null && userRepository.existsByEmail(email.trim());
    }
}
