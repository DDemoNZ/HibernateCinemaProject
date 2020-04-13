package com.dev.cinema.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private static Movie expectedFirstMockMovie;
    private static Movie expectedSecondMockMovie;
    private static Movie expectedThirdMockMovie;
    private static List<Movie> mockExpectedMoviesFromStorage;

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

        expectedFirstMockMovie = new Movie();
        expectedFirstMockMovie.setId(1L);
        expectedFirstMockMovie.setTitle("First");
        expectedFirstMockMovie.setDescription("First");

        expectedSecondMockMovie = new Movie();
        expectedSecondMockMovie.setId(2L);
        expectedSecondMockMovie.setTitle("Second");
        expectedSecondMockMovie.setDescription("Second");

        expectedThirdMockMovie = new Movie();
        expectedThirdMockMovie.setId(3L);
        expectedThirdMockMovie.setTitle("Third");
        expectedThirdMockMovie.setDescription("Third");

        mockExpectedMoviesFromStorage = new ArrayList<>();
        mockExpectedMoviesFromStorage.add(expectedFirstMockMovie);
        mockExpectedMoviesFromStorage.add(expectedSecondMockMovie);
        mockExpectedMoviesFromStorage.add(expectedThirdMockMovie);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addMovieOk() {
        when(movieService.add(anyObject())).thenReturn(mockMovie);

        MovieResponseDto movieResponseDto = movieController.addMovie(movieRequest);

        verify(movieService, times(1)).add(any());
        assertNotNull(movieResponseDto);
        assertEquals(movieRequest.getTitle(), movieResponseDto.getTitle());
        assertEquals(movieRequest.getDescription(), movieResponseDto.getDescription());
    }

    @Test
    void getAllMoviesOk() {
        when(movieService.getAll()).thenReturn(mockExpectedMoviesFromStorage);

        List<MovieResponseDto> allMovies = movieController.getAllMovies();

        verify(movieService, times(1)).getAll();
        assertNotNull(allMovies);
        assertNotNull(allMovies.get(1));
        assertEquals(expectedFirstMockMovie.getTitle(), allMovies.get(0).getTitle());
    }

    @Test
    void getAllMoviesNonexistent() {
        when(movieService.getAll()).thenReturn(new ArrayList<Movie>());

        List<MovieResponseDto> actualAllMoviesStorage = movieController.getAllMovies();

        verify(movieService, times(1)).getAll();
        assertNotNull(actualAllMoviesStorage);
        assertEquals(0, actualAllMoviesStorage.size());
    }
}
