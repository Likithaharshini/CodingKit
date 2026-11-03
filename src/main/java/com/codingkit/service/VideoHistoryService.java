package com.codingkit.service;

import com.codingkit.dto.VideoHistoryRequest;
import com.codingkit.entity.VideoHistory;

import java.util.List;

public interface VideoHistoryService {
    List<VideoHistory> getHistoryForCurrentUser();
    VideoHistory createVideoHistory(VideoHistoryRequest request);
    void deleteVideoHistory(Long id);
    VideoHistory getById(Long id);
}
