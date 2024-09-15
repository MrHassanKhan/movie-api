package com.inventrevo.movieapi.services;

import com.inventrevo.movieapi.dto.MovieDto;
import com.inventrevo.movieapi.dto.PaginatedResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface IMovieService {
    MovieDto addMovie(MovieDto movieDto, MultipartFile file, String username) throws IOException;
    MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file, String username) throws IOException;
    MovieDto getMovie(Integer movieId);
    List<MovieDto> getAllMovies();
    Integer deleteMovie(Integer movieId) throws IOException;

    PaginatedResponse<MovieDto> getPaginatedMovies(Integer pageNumber, Integer pageSize);

    PaginatedResponse<MovieDto> getPaginatedAndSortedMovies(Integer pageNumber, Integer pageSize, String sortBy, String direction);

    List<MovieDto> getMoviesWithAverageRating();
}
