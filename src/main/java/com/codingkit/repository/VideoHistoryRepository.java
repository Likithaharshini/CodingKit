package com.codingkit.repository;

import com.codingkit.entity.VideoHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoHistoryRepository extends JpaRepository<VideoHistory, Long> {
    List<VideoHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}
