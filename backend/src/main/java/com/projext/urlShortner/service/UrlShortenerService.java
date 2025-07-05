package com.projext.urlShortner.service;
import com.projext.urlShortner.model.ShortenRequest;

import com.projext.urlShort.model.UrlMapping;
import com.projext.urlShort.loggingMiddleware.logMiddleware;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

@Service
public class UrlShortenerService {
    private static final int DEFAULT_VALIDITY_MINUTES = 30;
    private static final int SHORTCODE_LENGTH = 6;
    private static final String HOST = "https://hostname:port/";
    private final Map<String, UrlMapping> urlStore = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public UrlMapping createShortUrl(String url, Integer validity, String customShortcode) {
        logMiddleware.log("urlshortener", "info", "service", "Received request to shorten URL: " + url);
        String shortcode = (customShortcode != null && !customShortcode.isEmpty()) ? customShortcode : generateShortcode();
        if (urlStore.containsKey(shortcode)) {
            throw new IllegalArgumentException("Shortcode already exists");
        }
        int validMinutes = (validity != null) ? validity : DEFAULT_VALIDITY_MINUTES;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = now.plusMinutes(validMinutes);
        UrlMapping mapping = new UrlMapping(url, shortcode, expiry, now);
        urlStore.put(shortcode, mapping);
        logMiddleware.log("urlshortener", "info", "service", "Short URL created: " + shortcode);
        return mapping;
    }

    public UrlMapping getOriginalUrl(String shortcode) {
        UrlMapping mapping = urlStore.get(shortcode);
        if (mapping == null) {
            logMiddleware.log("urlshortener", "error", "service", "Shortcode not found: " + shortcode);
            throw new IllegalArgumentException("Shortcode not found");
        }
        if (mapping.getExpiryTime().isBefore(LocalDateTime.now())) {
            logMiddleware.log("urlshortener", "error", "service", "Shortcode expired: " + shortcode);
            throw new IllegalArgumentException("Shortcode expired");
        }
        logMiddleware.log("urlshortener", "info", "service", "Redirecting to original URL for shortcode: " + shortcode);
        return mapping;
    }

    private String generateShortcode() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String code;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < SHORTCODE_LENGTH; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            code = sb.toString();
        } while (urlStore.containsKey(code));
        return code;
    }

    public String getShortLink(String shortcode) {
        return HOST + shortcode;
    }

    public String formatExpiry(LocalDateTime expiry) {
        return expiry.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
    }
} 