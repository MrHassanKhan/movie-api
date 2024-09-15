package com.inventrevo.movieapi.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record APIErrorResponse(
         HttpStatus status,
         String message,
         List<String> errors
) {
}
