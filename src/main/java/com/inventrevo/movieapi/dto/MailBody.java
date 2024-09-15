package com.inventrevo.movieapi.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record MailBody (String to, String subject, String body, Map<String, Object> variables) {
}
