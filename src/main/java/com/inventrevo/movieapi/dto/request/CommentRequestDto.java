package com.inventrevo.movieapi.dto.request;

import lombok.Data;

@Data
public class CommentRequestDto {
    private String content;
    private Integer movieId;
}
