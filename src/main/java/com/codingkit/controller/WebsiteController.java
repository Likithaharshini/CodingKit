package com.codingkit.controller;

import com.codingkit.dto.WebsiteRequest;
import com.codingkit.service.WebsiteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/websites")
public class WebsiteController {

    private final WebsiteService websiteService;

    public WebsiteController(WebsiteService websiteService) {
        this.websiteService = websiteService;
    }

    @PostMapping("/save")
    public String createWebsite(@Valid @ModelAttribute("websiteRequest") WebsiteRequest request,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Please enter a valid website.");
            return "dashboard/dashboard";
        }
        try {
            websiteService.createWebsite(request);
            redirectAttributes.addFlashAttribute("successMessage", "Website saved.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteWebsite(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            websiteService.deleteWebsite(id);
            redirectAttributes.addFlashAttribute("successMessage", "Website deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
