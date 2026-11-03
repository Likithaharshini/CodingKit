package com.codingkit.dto;

import jakarta.validation.constraints.NotBlank;

public class VideoHistoryRequest {

    @NotBlank(message = "Video URL is required")
    private String videoUrl;

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
}
