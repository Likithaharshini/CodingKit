package com.codingkit.controller;

import com.codingkit.dto.NoteRequest;
import com.codingkit.dto.VideoHistoryRequest;
import com.codingkit.dto.WebsiteRequest;
import com.codingkit.entity.Note;
import com.codingkit.entity.User;
import com.codingkit.entity.VideoHistory;
import com.codingkit.entity.Website;
import com.codingkit.service.NoteService;
import com.codingkit.service.UserService;
import com.codingkit.service.VideoHistoryService;
import com.codingkit.service.WebsiteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final WebsiteService websiteService;
    private final VideoHistoryService videoHistoryService;
    private final NoteService noteService;

    public DashboardController(UserService userService, WebsiteService websiteService,
                              VideoHistoryService videoHistoryService, NoteService noteService) {
        this.userService = userService;
        this.websiteService = websiteService;
        this.videoHistoryService = videoHistoryService;
        this.noteService = noteService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User currentUser = userService.getCurrentUser();
        List<Website> websites = websiteService.getWebsitesForCurrentUser();
        List<VideoHistory> history = videoHistoryService.getHistoryForCurrentUser();
        List<Note> notes = noteService.getNotesForCurrentUser();

        model.addAttribute("user", currentUser);
        model.addAttribute("websites", websites);
        model.addAttribute("videoHistory", history);
        model.addAttribute("notes", notes);
        model.addAttribute("websiteRequest", new WebsiteRequest());
        model.addAttribute("videoHistoryRequest", new VideoHistoryRequest());
        model.addAttribute("noteRequest", new NoteRequest());
        return "dashboard/dashboard";
    }

    @PostMapping("/dashboard/add-site")
    public String addSite(@Valid @ModelAttribute("websiteRequest") WebsiteRequest request,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please provide a valid website name and URL.");
            return "redirect:/dashboard";
        }
        try {
            websiteService.createWebsite(request);
            redirectAttributes.addFlashAttribute("successMessage", "Website added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/add-video")
    public String addVideo(@Valid @ModelAttribute("videoHistoryRequest") VideoHistoryRequest request,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please enter a valid YouTube URL.");
            return "redirect:/dashboard";
        }
        try {
            VideoHistory video = videoHistoryService.createVideoHistory(request);
            redirectAttributes.addFlashAttribute(
                    "videoEmbedUrl",
                    "https://www.youtube.com/embed/" + video.getVideoId() + "?autoplay=1&playsinline=1");
            redirectAttributes.addFlashAttribute("successMessage", "Video saved to your history.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/save-note")
    public String saveNote(@Valid @ModelAttribute("noteRequest") NoteRequest request,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Note title is required.");
            return "redirect:/dashboard";
        }
        try {
            noteService.createNote(request);
            redirectAttributes.addFlashAttribute("successMessage", "Note saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/delete-website/{websiteId}")
    public String deleteWebsite(@PathVariable Long websiteId, RedirectAttributes redirectAttributes) {
        try {
            websiteService.deleteWebsite(websiteId);
            redirectAttributes.addFlashAttribute("successMessage", "Website deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/delete-video/{videoId}")
    public String deleteVideo(@PathVariable Long videoId, RedirectAttributes redirectAttributes) {
        try {
            videoHistoryService.deleteVideoHistory(videoId);
            redirectAttributes.addFlashAttribute("successMessage", "Video removed from history.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/delete-note/{noteId}")
    public String deleteNote(@PathVariable Long noteId, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("successMessage", "Note deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
