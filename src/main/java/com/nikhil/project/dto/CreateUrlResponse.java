package com.nikhil.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateUrlResponse {
    private String shortLink;
    private LocalDateTime expiry;
}
