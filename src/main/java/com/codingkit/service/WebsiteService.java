package com.codingkit.service;

import com.codingkit.dto.WebsiteRequest;
import com.codingkit.entity.Website;

import java.util.List;

public interface WebsiteService {
    List<Website> getWebsitesForCurrentUser();
    Website createWebsite(WebsiteRequest request);
    Website updateWebsite(Long websiteId, WebsiteRequest request);
    void deleteWebsite(Long websiteId);
    Website getById(Long websiteId);
}
