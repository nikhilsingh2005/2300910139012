package com.nikhil.project.controller;

import com.nikhil.project.dto.CreateUrlRequest;
import com.nikhil.project.dto.CreateUrlResponse;
import com.nikhil.project.dto.UrlStatsResponse;
import com.nikhil.project.service.UrlService;
import com.nikhil.project.utility.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorturls")
    public ResponseEntity<CreateUrlResponse> createShortUrl(@Valid @RequestBody CreateUrlRequest request, UriComponentsBuilder ucb) {
        Log.log("backend", "info", "UrlController", "Received request to create short URL for: " + request.getUrl());
        String shortcode = urlService.createShortUrl(request);

        String shortLink = ucb.scheme("http").host("localhost").port(8080).path("/{shortcode}").buildAndExpand(shortcode).toUriString();

        CreateUrlResponse response = new CreateUrlResponse();
        response.setShortLink(shortLink);

        response.setExpiry(LocalDateTime.parse(urlService.getUrlStats(shortcode).getExpiryDate().format(DateTimeFormatter.ISO_INSTANT)));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortcode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortcode, HttpServletRequest request) {
        String originalUrl = urlService.getOriginalUrlAndLogClick(shortcode, request.getRemoteAddr(), request.getHeader("referer"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", originalUrl);

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/shorturls/{shortcode}")
    public ResponseEntity<UrlStatsResponse> getShortUrlStats(@PathVariable String shortcode) {
        Log.log("backend", "info", "UrlController", "Received stats request for shortcode: " + shortcode);
        UrlStatsResponse stats = urlService.getUrlStats(shortcode);
        return ResponseEntity.ok(stats);
    }
}
