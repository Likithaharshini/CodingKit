package com.codingkit.service.impl;

import com.codingkit.dto.NoteRequest;
import com.codingkit.entity.Note;
import com.codingkit.entity.User;
import com.codingkit.exception.ResourceNotFoundException;
import com.codingkit.exception.UnauthorizedAccessException;
import com.codingkit.repository.NoteRepository;
import com.codingkit.service.NoteService;
import com.codingkit.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;

    public NoteServiceImpl(NoteRepository noteRepository, UserService userService) {
        this.noteRepository = noteRepository;
        this.userService = userService;
    }

    @Override
    public List<Note> getNotesForCurrentUser() {
        User user = userService.getCurrentUser();
        return noteRepository.findByUserIdOrderByUpdatedAtDesc(user.getId());
    }

    @Override
    @Transactional
    public Note createNote(NoteRequest request) {
        User user = userService.getCurrentUser();
        String title = request.getTitle() == null ? "" : request.getTitle().trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Note title is required.");
        }
        Note note = new Note(title, request.getContent(), user);
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public Note updateNote(Long noteId, NoteRequest request) {
        User user = userService.getCurrentUser();
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found."));
        if (!note.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have access to this note.");
        }
        note.setTitle(request.getTitle().trim());
        note.setContent(request.getContent());
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public void deleteNote(Long noteId) {
        User user = userService.getCurrentUser();
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found."));
        if (!note.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have access to this note.");
        }
        noteRepository.delete(note);
    }

    @Override
    public Note getById(Long noteId) {
        User user = userService.getCurrentUser();
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found."));
        if (!note.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have access to this note.");
        }
        return note;
    }
}
