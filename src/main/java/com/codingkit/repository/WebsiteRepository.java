package com.codingkit.repository;

import com.codingkit.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebsiteRepository extends JpaRepository<Website, Long> {
    List<Website> findByUserIdOrderByCreatedAtDesc(Long userId);
}
