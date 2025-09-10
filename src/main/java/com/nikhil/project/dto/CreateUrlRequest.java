package com.nikhil.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateUrlRequest {
    private String url;
    private Integer validity;
    private String shortcode;
}
