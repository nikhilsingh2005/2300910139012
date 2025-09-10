package com.nikhil.project.service;

import com.nikhil.project.dto.CreateUrlRequest;
import com.nikhil.project.dto.UrlStatsResponse;
import com.nikhil.project.entity.ClickEvent;
import com.nikhil.project.entity.UrlEntry;
import com.nikhil.project.exception.ShortcodeCollisionException;
import com.nikhil.project.exception.ShortcodeNotFoundException;
import com.nikhil.project.repository.ClickEventRepository;
import com.nikhil.project.repository.UrlRepository;
import com.nikhil.project.utility.Log;
import com.nikhil.project.utility.ShortcodeGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UrlService {
    private static final int DEFAULT_VALIDITY_MINUTES = 30;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ClickEventRepository clickEventRepository;

    public String createShortUrl(CreateUrlRequest request) {
        String shortcode;

        if (request.getShortcode() != null && !request.getShortcode().isBlank()) {
            shortcode = request.getShortcode();
            Optional<UrlEntry> existingEntry = urlRepository.findByShortcode(shortcode);
            if (existingEntry.isPresent()) {
                Log.log("backend", "error", "UrlService", "Shortcode collision detected for: " + shortcode);
                throw new ShortcodeCollisionException("Custom shortcode is already in use.");
            }
        } else {
            shortcode = ShortcodeGenerator.generateUniqueShortcode();
        }

        UrlEntry urlEntry = new UrlEntry();
        urlEntry.setShortcode(shortcode);
        urlEntry.setOriginalUrl(request.getUrl());
        urlEntry.setCreatedAt(LocalDateTime.now());

        int validity = (request.getValidity() != null) ? request.getValidity() : DEFAULT_VALIDITY_MINUTES;
        urlEntry.setExpiresAt(urlEntry.getCreatedAt().plusMinutes(validity));

        urlRepository.save(urlEntry);
        Log.log("backend", "info", "UrlService", "Successfully created new shortcode: " + shortcode);
        return shortcode;
    }

    @Transactional
    public String getOriginalUrlAndLogClick(String shortcode, String ipAddress, String referrer) {
        UrlEntry urlEntry = urlRepository.findByShortcode(shortcode)
                .orElseThrow(() -> new ShortcodeNotFoundException("Shortcode not found or expired."));

        if (urlEntry.getExpiresAt() != null && urlEntry.getExpiresAt().isBefore(LocalDateTime.now())) {
            Log.log("backend", "error", "UrlService", "Attempted access to expired shortcode: " + shortcode);
            throw new ShortcodeNotFoundException("Shortcode not found or expired.");
        }

        urlEntry.setClickCount(urlEntry.getClickCount() + 1);
        urlRepository.save(urlEntry);

        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setShortcode(shortcode);
        clickEvent.setTimestamp(LocalDateTime.now());
        clickEvent.setIpAddress(ipAddress);
        clickEvent.setReferrer(referrer);
        clickEventRepository.save(clickEvent);

        Log.log("backend", "info", "UrlService", "Redirecting shortcode " + shortcode + " to " + urlEntry.getOriginalUrl());
        return urlEntry.getOriginalUrl();
    }

    public UrlStatsResponse getUrlStats(String shortcode) {
        UrlEntry urlEntry = urlRepository.findByShortcode(shortcode)
                .orElseThrow(() -> new ShortcodeNotFoundException("Shortcode not found."));

        List<ClickEvent> clickEvents = clickEventRepository.findByShortcodeOrderByTimestampAsc(shortcode);

        UrlStatsResponse response = new UrlStatsResponse();
        response.setTotalClicks(urlEntry.getClickCount());
        response.setOriginalUrl(urlEntry.getOriginalUrl());
        response.setCreationDate(urlEntry.getCreatedAt());
        response.setExpiryDate(urlEntry.getExpiresAt());
        response.setDetailedClicks(clickEvents);

        Log.log("backend", "info", "UrlService", "Retrieved stats for shortcode: " + shortcode);
        return response;
    }
}