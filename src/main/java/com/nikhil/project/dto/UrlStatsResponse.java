package com.nikhil.project.dto;

import com.nikhil.project.entity.ClickEvent;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UrlStatsResponse {
    private long totalClicks;
    private String originalUrl;
    private LocalDateTime creationDate;
    private LocalDateTime expiryDate;
    private List<ClickEvent> detailedClicks;
}