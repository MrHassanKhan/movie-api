package com.inventrevo.movieapi.services;

import com.inventrevo.movieapi.dto.request.CommentRequestDto;
import com.inventrevo.movieapi.entities.Comment;
import com.inventrevo.movieapi.repositories.CommentRepository;
import com.inventrevo.movieapi.repositories.MovieRepository;
import com.inventrevo.movieapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    public Comment saveComment(CommentRequestDto commentRequestDto, String username) {
        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .user(userRepository.findByUsername(username).get())
                .movie(movieRepository.findById(commentRequestDto.getMovieId()).get())
                .build();

        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment updateComment(CommentRequestDto commentRequestDto, Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.setContent(commentRequestDto.getContent());
        return commentRepository.save(comment);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).get();
    }

    public List<Comment> getAllCommentByMovieId(Integer movieId) {
        return commentRepository.findCommentsByMovie_MovieId(movieId).get();
    }

    public List<Comment> getAllCommentByUserId(String username) {
        return commentRepository.findCommentsByUserUsername(username).get();
    }
}
