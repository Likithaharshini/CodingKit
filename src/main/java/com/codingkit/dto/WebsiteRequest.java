package com.codingkit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class WebsiteRequest {

    @NotBlank(message = "Website name is required")
    private String websiteName;

    @NotBlank(message = "Website URL is required")
    @Pattern(regexp = "^(https?:\\/\\/).+", message = "Please enter a valid URL starting with http:// or https://")
    private String websiteUrl;

    public String getWebsiteName() { return websiteName; }
    public void setWebsiteName(String websiteName) { this.websiteName = websiteName; }

    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }
}
