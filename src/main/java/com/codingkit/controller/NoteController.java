package com.codingkit.controller;

import com.codingkit.dto.NoteRequest;
import com.codingkit.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/save")
    public String saveNote(@Valid @ModelAttribute("noteRequest") NoteRequest request,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Note title is required.");
            return "dashboard/dashboard";
        }
        try {
            noteService.createNote(request);
            redirectAttributes.addFlashAttribute("successMessage", "Note saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
