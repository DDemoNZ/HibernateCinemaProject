package com.dev.cinema.controllers;

import com.dev.cinema.model.Movie;
import com.dev.cinema.model.dto.request.MovieRequestDto;
import com.dev.cinema.model.dto.response.MovieResponseDto;
import com.dev.cinema.service.MovieService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public MovieResponseDto addMovie(@RequestBody @Valid MovieRequestDto movieRequestDto) {
        Movie movie = movieFromRequestDto(movieRequestDto);
        movieService.add(movie);
        return getMovieResponseDto(movie);
    }

    @GetMapping("/all")
    public List<MovieResponseDto> getAllMovies() {
        return movieService.getAll().stream()
                .map(this::getMovieResponseDto)
                .collect(Collectors.toList());
    }

    @PutMapping
    public MovieResponseDto updateMovie(@RequestBody @Valid MovieRequestDto movieRequestDto,
                                        Long movieId) {
        Movie movie = movieFromRequestDto(movieRequestDto);
        movie.setId(movieId);
        Movie updatedMovie = movieService.update(movie);
        return getMovieResponseDto(updatedMovie);
    }

    private Movie movieFromRequestDto(MovieRequestDto movieRequestDto) {
        Movie movie = new Movie();
        movie.setTitle(movieRequestDto.getTitle());
        movie.setDescription(movieRequestDto.getDescription());
        return movie;
    }

    private MovieResponseDto getMovieResponseDto(Movie movie) {
        MovieResponseDto movieResponseDto = new MovieResponseDto();
        movieResponseDto.setDescription(movie.getDescription());
        movieResponseDto.setTitle(movie.getTitle());
        return movieResponseDto;
    }

}
