package com.inventrevo.movieapi.dto.response;

import com.inventrevo.movieapi.entities.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Integer movieId;
    private String userFullName;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.movieId = comment.getMovie().getMovieId();
        this.userFullName = comment.getUser().getName();
    }
}
