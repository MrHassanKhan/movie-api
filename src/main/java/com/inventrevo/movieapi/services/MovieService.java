package com.inventrevo.movieapi.services;

import com.inventrevo.movieapi.dto.MovieDto;
import com.inventrevo.movieapi.dto.PaginatedResponse;
import com.inventrevo.movieapi.entities.Movie;
import com.inventrevo.movieapi.entities.Tag;
import com.inventrevo.movieapi.repositories.MovieRepository;
import com.inventrevo.movieapi.repositories.TagRepository;
import com.inventrevo.movieapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MovieService implements IMovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private FileService fileService;
    @Value("${project.poster}")
    private String path;
    @Value("${base.url}")
    private String baseUrl;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file, String username) throws IOException {
        if(Files.exists(Paths.get(path + file.getOriginalFilename()))) {
            throw new RuntimeException("File already exists! Please choose a different one filename.");
        }
        String uploadedFileName = fileService.uploadFile(path, file);
        movieDto.setPoster(uploadedFileName);
        Movie movie = new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster(),
                userRepository.findByUsername(username).get(),
                movieDto.getTagNames().stream().map(name -> tagRepository.findByName(name)).toList()
        );
        Movie savedMovie = movieRepository.save(movie);
        String posterUrl = baseUrl + "/file/" + uploadedFileName;
        movieDto.setPoster(savedMovie.getPoster());
        movieDto.setMovieId(savedMovie.getMovieId());
        movieDto.setPosterUrl(posterUrl);
        return movieDto;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file, String username) throws IOException {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
        String fileName = movie.getPoster();
        if(file != null) {
            Files.deleteIfExists(Paths.get(path + fileName));
            fileName = fileService.uploadFile(path, file);
        }
        movieDto.setPoster(fileName);
        Movie updatedMovie = new Movie(
                movie.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster(),
                userRepository.findByUsername(username).get(),
                movieDto.getTagNames().stream().map(name -> tagRepository.findByName(name)).toList()
        );
        movieRepository.save(updatedMovie);
        String posterUrl = baseUrl + "/file/" + fileName;
        movieDto.setPoster(updatedMovie.getPoster());
        movieDto.setMovieId(updatedMovie.getMovieId());
        movieDto.setPosterUrl(posterUrl);
        return movieDto;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
        String posterUrl = baseUrl + "/file/" + movie.getPoster();
        return new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl,
                movie.getUser().getUsername(),
                movie.getTags().stream().map(tag -> tag.getClass().getName()).toList()
        );
    }

    @Override
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream().map(movie -> {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            return new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl,
                    movie.getUser().getUsername(),
                    movie.getTags().stream().map(tag -> tag.getName()).toList()
            );
        }).toList();
    }

    @Override
    public Integer deleteMovie(Integer movieId) throws IOException {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie Not found"));
        movieRepository.deleteById(movieId);
        Files.deleteIfExists(Paths.get(path + movie.getPoster()));
        return movieId;
    }

    @Override
    public PaginatedResponse<MovieDto> getPaginatedMovies(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> movies = movieRepository.findAll(pageable);
        List<MovieDto> movieDtos = new ArrayList<>();
        movies.forEach(movie -> {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDtos.add(new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl,
                    movie.getUser().getUsername(),
                    movie.getTags().stream().map(tag -> tag.getName()).toList()
            ));
        });

        return new PaginatedResponse<>(movieDtos, pageNumber, pageSize, movies.getTotalPages(), movies.getTotalElements(), movies.isLast());
    }

    @Override
    public PaginatedResponse<MovieDto> getPaginatedAndSortedMovies(Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy) : Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movie> movies = movieRepository.findAll(pageable);
        List<MovieDto> movieDtos = new ArrayList<>();
        movies.forEach(movie -> {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDtos.add(new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl,
                    movie.getUser().getUsername(),
                    movie.getTags().stream().map(tag -> tag.getName()).toList()
            ));
        });
        return new PaginatedResponse<>(movieDtos, pageNumber, pageSize, movies.getTotalPages(), movies.getTotalElements(), movies.isLast());
    }

    @Override
    public List<MovieDto> getMoviesWithAverageRating() {
        return movieRepository.getMoviesWithAverageRating();
    }
}
