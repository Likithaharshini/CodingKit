package com.codingkit.service;

import com.codingkit.dto.RegisterRequest;
import com.codingkit.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    User getCurrentUser();
    User findByUsernameOrEmail(String value);
    User updateProfile(Long userId, String fullName, String username, String email);
    void changePassword(Long userId, String currentPassword, String newPassword, String confirmPassword);
    boolean isDuplicateUsername(String username);
    boolean isDuplicateEmail(String email);
}
