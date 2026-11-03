package com.codingkit.service;

import com.codingkit.dto.NoteRequest;
import com.codingkit.entity.Note;

import java.util.List;

public interface NoteService {
    List<Note> getNotesForCurrentUser();
    Note createNote(NoteRequest request);
    Note updateNote(Long noteId, NoteRequest request);
    void deleteNote(Long noteId);
    Note getById(Long noteId);
}
