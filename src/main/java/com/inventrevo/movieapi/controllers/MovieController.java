package com.inventrevo.movieapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventrevo.movieapi.dto.MovieDto;
import com.inventrevo.movieapi.dto.PaginatedResponse;
import com.inventrevo.movieapi.services.MovieService;
import com.inventrevo.movieapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

//    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDto dto = objectMapper.readValue(movieDto, MovieDto.class);
        if(file.isEmpty()) {
            file = null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(movieService.addMovie(dto, file, authentication.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId) {
        return new ResponseEntity<>(movieService.getMovie(movieId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMoviesHandler() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<Integer> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        return new ResponseEntity<>(movieService.deleteMovie(movieId), HttpStatus.OK);
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId, @RequestPart(required = false) MultipartFile file,
                                                       @RequestPart String movieDto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDto dto = objectMapper.readValue(movieDto, MovieDto.class);
        if(file == null || file.isEmpty()) {
            file = null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return new ResponseEntity<>(movieService.updateMovie(movieId, dto, file, authentication.getName()), HttpStatus.OK);
    }


    @GetMapping("/paginatedMovies")
    public ResponseEntity<PaginatedResponse<MovieDto>> getPaginatedMoviesHandler(
            @RequestParam(defaultValue = AppConstants.Page_Number) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.Page_Size) Integer pageSize) {
        return new ResponseEntity<PaginatedResponse<MovieDto>>(movieService.getPaginatedMovies(pageNumber, pageSize), HttpStatus.OK);
    }


    @GetMapping("/paginatedAndSortedMovies")
    public ResponseEntity<PaginatedResponse<MovieDto>> getPaginatedAndSortedMoviesHandler(
            @RequestParam(defaultValue = AppConstants.Page_Number) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.Page_Size) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.Sort_By) String sortBy,
            @RequestParam(defaultValue = AppConstants.Sort_Direction) String direction) {
        return new ResponseEntity<PaginatedResponse<MovieDto>>(movieService.getPaginatedAndSortedMovies(pageNumber, pageSize, sortBy, direction), HttpStatus.OK);
    }

    @GetMapping("/getMoviesWithAverageRating")
    public ResponseEntity<List<MovieDto>> getMoviesWithAverageRating() {
        return new ResponseEntity<>(movieService.getMoviesWithAverageRating(), HttpStatus.OK);
    }

}
