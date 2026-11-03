package com.codingkit.controller;

import com.codingkit.dto.VideoHistoryRequest;
import com.codingkit.service.VideoHistoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/history")
public class VideoHistoryController {

    private final VideoHistoryService videoHistoryService;

    public VideoHistoryController(VideoHistoryService videoHistoryService) {
        this.videoHistoryService = videoHistoryService;
    }

    @PostMapping("/save-video")
    public String saveVideo(@Valid @ModelAttribute("videoHistoryRequest") VideoHistoryRequest request,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Please provide a valid YouTube URL.");
            return "dashboard/dashboard";
        }
        try {
            videoHistoryService.createVideoHistory(request);
            redirectAttributes.addFlashAttribute("successMessage", "Video saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
