package com.codingkit.repository;

import com.codingkit.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserIdOrderByUpdatedAtDesc(Long userId);
}
