package com.codingkit.service.impl;

import com.codingkit.dto.VideoHistoryRequest;
import com.codingkit.entity.User;
import com.codingkit.entity.VideoHistory;
import com.codingkit.exception.InvalidVideoUrlException;
import com.codingkit.exception.ResourceNotFoundException;
import com.codingkit.exception.UnauthorizedAccessException;
import com.codingkit.repository.VideoHistoryRepository;
import com.codingkit.service.UserService;
import com.codingkit.service.VideoHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VideoHistoryServiceImpl implements VideoHistoryService {

    private static final Pattern YOUTUBE_PATTERN = Pattern.compile("(?:https?:\\/\\/)?(?:www\\.|m\\.)?(?:youtube\\.com\\/(?:watch\\?v=|shorts\\/)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})");

    private final VideoHistoryRepository videoHistoryRepository;
    private final UserService userService;

    public VideoHistoryServiceImpl(VideoHistoryRepository videoHistoryRepository, UserService userService) {
        this.videoHistoryRepository = videoHistoryRepository;
        this.userService = userService;
    }

    @Override
    public List<VideoHistory> getHistoryForCurrentUser() {
        User user = userService.getCurrentUser();
        return videoHistoryRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @Override
    @Transactional
    public VideoHistory createVideoHistory(VideoHistoryRequest request) {
        String rawUrl = request.getVideoUrl();
        String videoUrl = rawUrl == null ? "" : rawUrl.trim();
        Matcher matcher = YOUTUBE_PATTERN.matcher(videoUrl);
        if (!matcher.find()) {
            throw new InvalidVideoUrlException("Please enter a valid YouTube URL.");
        }

        String videoId = matcher.group(1);
        String title = "YouTube Video - " + videoId;

        User user = userService.getCurrentUser();
        VideoHistory videoHistory = new VideoHistory(videoUrl, videoId, title, user);
        return videoHistoryRepository.save(videoHistory);
    }

    @Override
    @Transactional
    public void deleteVideoHistory(Long id) {
        User user = userService.getCurrentUser();
        VideoHistory videoHistory = videoHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video history not found."));
        if (!videoHistory.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have access to this video history.");
        }
        videoHistoryRepository.delete(videoHistory);
    }

    @Override
    public VideoHistory getById(Long id) {
        User user = userService.getCurrentUser();
        VideoHistory videoHistory = videoHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video history not found."));
        if (!videoHistory.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have access to this video history.");
        }
        return videoHistory;
    }
}
