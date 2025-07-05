package com.projext.urlShortener.controller;

import com.projext.urlShort.model.ShortenRequest;
import com.projext.urlShort.model.ShortenResponse;
import com.projext.urlShort.model.UrlMapping;
import com.projext.urlShort.service.UrlShortenerService;
import loggingMiddleware.logMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UrlShortenerController {
    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorturls")
    public ResponseEntity<ShortenResponse> createShortUrl(@RequestBody ShortenRequest request) {
        logMiddleware.log("urlshortener", "info", "controller", "POST /shorturls called");
        if (request.getUrl() == null || request.getUrl().isEmpty()) {
            logMiddleware.log("urlshortener", "error", "controller", "Missing URL in request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        UrlMapping mapping = urlShortenerService.createShortUrl(request.getUrl(), request.getValidity(), request.getShortcode());
        String shortLink = urlShortenerService.getShortLink(mapping.getShortcode());
        String expiry = urlShortenerService.formatExpiry(mapping.getExpiryTime());
        ShortenResponse response = new ShortenResponse(shortLink, expiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortcode}")
    public RedirectView redirectToOriginal(@PathVariable String shortcode) {
        logMiddleware.log("urlshortener", "info", "controller", "GET /" + shortcode + " called");
        UrlMapping mapping = urlShortenerService.getOriginalUrl(shortcode);
        return new RedirectView(mapping.getOriginalUrl());
    }
} 
