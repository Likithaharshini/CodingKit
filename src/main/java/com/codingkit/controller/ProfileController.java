package com.codingkit.controller;

import com.codingkit.dto.PasswordChangeRequest;
import com.codingkit.dto.ProfileUpdateRequest;
import com.codingkit.entity.User;
import com.codingkit.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        User user = userService.getCurrentUser();
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setFullName(user.getFullName());
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());

        model.addAttribute("user", user);
        model.addAttribute("profileUpdateRequest", request);
        model.addAttribute("passwordChangeRequest", new PasswordChangeRequest());
        return "profile/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@Valid @ModelAttribute("profileUpdateRequest") ProfileUpdateRequest request,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "profile/profile";
        }

        try {
            User currentUser = userService.getCurrentUser();
            userService.updateProfile(currentUser.getId(), request.getFullName(), request.getUsername(), request.getEmail());
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@Valid @ModelAttribute("passwordChangeRequest") PasswordChangeRequest request,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "profile/profile";
        }
        try {
            User currentUser = userService.getCurrentUser();
            userService.changePassword(currentUser.getId(), request.getCurrentPassword(), request.getNewPassword(), request.getConfirmPassword());
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }
}
