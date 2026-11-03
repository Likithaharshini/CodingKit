package com.codingkit.service.impl;

import com.codingkit.dto.WebsiteRequest;
import com.codingkit.entity.User;
import com.codingkit.entity.Website;
import com.codingkit.exception.ResourceNotFoundException;
import com.codingkit.exception.UnauthorizedAccessException;
import com.codingkit.repository.WebsiteRepository;
import com.codingkit.service.UserService;
import com.codingkit.service.WebsiteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WebsiteServiceImpl implements WebsiteService {

    private final WebsiteRepository websiteRepository;
    private final UserService userService;

    public WebsiteServiceImpl(WebsiteRepository websiteRepository, UserService userService) {
        this.websiteRepository = websiteRepository;
        this.userService = userService;
    }

    @Override
    public List<Website> getWebsitesForCurrentUser() {
        User user = userService.getCurrentUser();
        return websiteRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @Override
    @Transactional
    public Website createWebsite(WebsiteRequest request) {
        User user = userService.getCurrentUser();
        String name = request.getWebsiteName() == null ? "" : request.getWebsiteName().trim();
        String url = request.getWebsiteUrl() == null ? "" : request.getWebsiteUrl().trim();

        if (name.isEmpty() || url.isEmpty()) {
            throw new IllegalArgumentException("Website name and URL are required.");
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("Please enter a valid website URL.");
        }

        Website website = new Website(name, url, user);
        return websiteRepository.save(website);
    }

    @Override
    @Transactional
    public Website updateWebsite(Long websiteId, WebsiteRequest request) {
        User user = userService.getCurrentUser();
        Website website = websiteRepository.findById(websiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Website not found."));
        if (!website.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have access to this website.");
        }
        website.setWebsiteName(request.getWebsiteName().trim());
        website.setWebsiteUrl(request.getWebsiteUrl().trim());
        return websiteRepository.save(website);
    }

    @Override
    @Transactional
    public void deleteWebsite(Long websiteId) {
        User user = userService.getCurrentUser();
        Website website = websiteRepository.findById(websiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Website not found."));
        if (!website.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have access to this website.");
        }
        websiteRepository.delete(website);
    }

    @Override
    public Website getById(Long websiteId) {
        User user = userService.getCurrentUser();
        Website website = websiteRepository.findById(websiteId)
                .orElseThrow(() -> new ResourceNotFoundException("Website not found."));
        if (!website.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have access to this website.");
        }
        return website;
    }
}
