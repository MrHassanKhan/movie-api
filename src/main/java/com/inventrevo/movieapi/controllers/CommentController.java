package com.inventrevo.movieapi.controllers;

import com.inventrevo.movieapi.dto.request.CommentRequestDto;
import com.inventrevo.movieapi.dto.response.CommentResponseDto;
import com.inventrevo.movieapi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/getCommentsByMovieId/{movieId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByMovieId(@PathVariable Integer movieId) {
        return ResponseEntity.ok(commentService.getAllCommentByMovieId(movieId).stream().map(CommentResponseDto::new).toList());
    }

    @GetMapping("/getCommentsByUserId/{username}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByUserId(@PathVariable String username) {
        return ResponseEntity.ok(commentService.getAllCommentByUserId(username).stream().map(CommentResponseDto::new).toList());
    }

    @PostMapping("/addComment")
    public ResponseEntity<CommentResponseDto> addComment(@RequestBody CommentRequestDto commentRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(new CommentResponseDto(commentService.saveComment(commentRequestDto, authentication.getName())));
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PutMapping("/updateComment/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@RequestBody CommentRequestDto commentRequestDto, @PathVariable Long commentId) {
        return ResponseEntity.ok(new CommentResponseDto(commentService.updateComment(commentRequestDto, commentId)));
    }
}
