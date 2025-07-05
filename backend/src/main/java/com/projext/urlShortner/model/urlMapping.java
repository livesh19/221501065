package com.projext.urlShortner.model;

import java.time.LocalDateTime;

public class UrlMapping {
    private String originalUrl;
    private String shortcode;
    private LocalDateTime expiryTime;
    private LocalDateTime createdAt;

    public UrlMapping(String originalUrl, String shortcode, LocalDateTime expiryTime, LocalDateTime createdAt) {
        this.originalUrl = originalUrl;
        this.shortcode = shortcode;
        this.expiryTime = expiryTime;
        this.createdAt = createdAt;
    }

    public String getOriginalUrl() { return originalUrl; }
    public String getShortcode() { return shortcode; }
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
} 