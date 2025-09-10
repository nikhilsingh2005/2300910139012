package com.nikhil.project.service;

import com.nikhil.project.dto.LogEntry;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoggingService {

    private final String LOG_API_URL = "http://20.244.56.144/evaluation-service/logs";
    private final RestTemplate restTemplate = new RestTemplate();

    public void log(String stack, String level, String packageName, String message) {
        try {
            LogEntry logEntry = new LogEntry(stack, level, packageName, message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<LogEntry> request = new HttpEntity<>(logEntry, headers);
            restTemplate.postForEntity(LOG_API_URL, request, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send log: " + e.getMessage());
        }
    }
}
