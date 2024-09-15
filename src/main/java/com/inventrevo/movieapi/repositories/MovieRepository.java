package com.inventrevo.movieapi.repositories;

import com.inventrevo.movieapi.dto.MovieDto;
import com.inventrevo.movieapi.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {


    @Query("SELECT m, AVG(r.rating) FROM Movie m left join m.ratings r group by m.movieId")
    List<MovieDto> getMoviesWithAverageRating();
}
