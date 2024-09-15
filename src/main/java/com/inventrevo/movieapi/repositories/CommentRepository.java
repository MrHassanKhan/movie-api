package com.inventrevo.movieapi.repositories;

import com.inventrevo.movieapi.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findCommentsByMovie_MovieId(Integer movieId);

    Optional<List<Comment>> findCommentsByUserUsername(String username);
}
