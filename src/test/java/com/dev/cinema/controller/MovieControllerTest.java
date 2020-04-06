package com.dev.cinema.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.controllers.MovieController;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.dto.request.MovieRequestDto;
import com.dev.cinema.model.dto.response.MovieResponseDto;
import com.dev.cinema.service.impl.MovieServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MovieControllerTest {

    private static MovieRequestDto movieRequest;
    private static Movie mockMovie;
    private static Movie first;
    private static Movie second;
    private static Movie third;
    private static List<Movie> moviesFromStorage;

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieServiceImpl movieService;

    @BeforeAll
    static void beforeAll() {
        movieRequest = new MovieRequestDto();
        movieRequest.setTitle("Test");
        movieRequest.setDescription("Test");

        mockMovie = new Movie();
        mockMovie.setId(1L);
        mockMovie.setTitle(movieRequest.getTitle());
        mockMovie.setDescription(movieRequest.getDescription());

        first = new Movie();
        first.setId(1L);
        first.setTitle("First");
        first.setDescription("First");
        second = new Movie();
        second.setId(2L);
        second.setTitle("Second");
        second.setDescription("Second");
        third = new Movie();
        third.setId(3L);
        third.setTitle("Third");
        third.setDescription("Third");

        moviesFromStorage = new ArrayList<>();
        moviesFromStorage.add(first);
        moviesFromStorage.add(second);
        moviesFromStorage.add(third);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addMovie() {
        when(movieService.add(anyObject())).thenReturn(mockMovie);

        MovieResponseDto movieResponseDto = movieController.addMovie(movieRequest);

        verify(movieService, times(1)).add(any());
        assertNotNull(movieResponseDto);
        assertEquals(movieRequest.getTitle(), movieResponseDto.getTitle());
        assertEquals(movieRequest.getDescription(), movieResponseDto.getDescription());
    }

    @Test
    void getAllMovies() {
        when(movieService.getAll()).thenReturn(moviesFromStorage);

        List<MovieResponseDto> allMovies = movieController.getAllMovies();

        verify(movieService, times(1)).getAll();
        assertNotNull(allMovies);
        assertNotNull(allMovies.get(1));
        assertEquals(first.getTitle(), allMovies.get(0).getTitle());
    }
}