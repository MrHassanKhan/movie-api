package com.inventrevo.movieapi.dto;

import com.inventrevo.movieapi.entities.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private Integer movieId;
    @NotBlank(message = "Please provide Movie's title!")
    private String title;
    @NotBlank(message = "Please provide Movie's director!")
    private String director;
    @NotBlank(message = "Please provide Movie's studio!")
    private String studio;
    private Set<String> movieCast;
    @NotNull(message = "Please provide Movie's release year!")
    private Integer releaseYear;
    @NotBlank(message = "Please provide Movie's poster!")
    private String poster;
    @NotBlank(message = "Please provide Movie's posterUrl!")
    private String posterUrl;
    private Optional<String> username;
    private Double averageRating;
    private List<String> tagNames;

    public MovieDto(Integer movieId, String title, String director, String studio, Set<String> movieCast, Integer releaseYear, String poster, String posterUrl, String username, List<String> tagNames) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.studio = studio;
        this.movieCast = movieCast;
        this.releaseYear = releaseYear;
        this.poster = poster;
        this.posterUrl = posterUrl;
        this.username = Optional.ofNullable(username);
        this.tagNames = tagNames;
    }
}
