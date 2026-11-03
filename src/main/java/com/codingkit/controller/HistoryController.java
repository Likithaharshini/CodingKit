package com.codingkit.controller;

import com.codingkit.entity.Note;
import com.codingkit.entity.User;
import com.codingkit.entity.VideoHistory;
import com.codingkit.service.NoteService;
import com.codingkit.service.UserService;
import com.codingkit.service.VideoHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HistoryController {

    private final UserService userService;
    private final VideoHistoryService videoHistoryService;
    private final NoteService noteService;

    public HistoryController(UserService userService, VideoHistoryService videoHistoryService, NoteService noteService) {
        this.userService = userService;
        this.videoHistoryService = videoHistoryService;
        this.noteService = noteService;
    }

    @GetMapping("/history")
    public String historyPage(Model model) {
        User currentUser = userService.getCurrentUser();
        List<VideoHistory> history = videoHistoryService.getHistoryForCurrentUser();
        List<Note> notes = noteService.getNotesForCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("history", history);
        model.addAttribute("notes", notes);
        return "history/history";
    }

    @PostMapping("/history/delete-video/{videoId}")
    public String deleteVideoHistory(@PathVariable Long videoId, RedirectAttributes redirectAttributes) {
        try {
            videoHistoryService.deleteVideoHistory(videoId);
            redirectAttributes.addFlashAttribute("successMessage", "Video deleted from history.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/history";
    }

    @PostMapping("/history/delete-note/{noteId}")
    public String deleteNoteHistory(@PathVariable Long noteId, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("successMessage", "Note deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/history";
    }
}
