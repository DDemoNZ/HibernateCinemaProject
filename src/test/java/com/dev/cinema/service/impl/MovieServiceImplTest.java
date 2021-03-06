package com.dev.cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.dao.MovieDao;
import com.dev.cinema.model.Movie;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MovieServiceImplTest {

    private static Movie firstMovie;
    private static Movie secondMovie;
    private static Movie thirdMovie;
    private static List<Movie> movieStorage;
    private static Movie expectedMockMovie;
    private static Movie testMovie;

    @InjectMocks
    private MovieServiceImpl movieService;

    @Mock
    private MovieDao movieDao;

    @BeforeAll
    public static void beforeAll() {
        expectedMockMovie = new Movie();
        expectedMockMovie.setId(1L);
        expectedMockMovie.setTitle("Test");
        expectedMockMovie.setDescription("Test");

        testMovie = new Movie();
        testMovie.setTitle("Test");
        testMovie.setDescription("Test");

        firstMovie = new Movie();
        firstMovie.setId(1L);
        firstMovie.setTitle("First");
        firstMovie.setDescription("First");

        secondMovie = new Movie();
        secondMovie.setId(2L);
        secondMovie.setTitle("Second");
        secondMovie.setDescription("Second");

        thirdMovie = new Movie();
        thirdMovie.setId(3L);
        thirdMovie.setTitle("Third");
        thirdMovie.setDescription("Third");

        movieStorage = new ArrayList<>();
        movieStorage.add(firstMovie);
        movieStorage.add(secondMovie);
        movieStorage.add(thirdMovie);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addMovieOk() {
        when(movieDao.add(testMovie)).thenReturn(expectedMockMovie);

        Movie addedMovie = movieService.add(testMovie);

        verify(movieDao, times(1)).add(any());

        assertNotNull(addedMovie.getId());
        assertEquals(Long.valueOf(1L), addedMovie.getId());
        assertEquals("Test", addedMovie.getTitle());
    }

    @Test
    public void getAllMoviesOk() {
        when(movieDao.getAll()).thenReturn(movieStorage);

        List<Movie> actualMoviesFromStorage = movieService.getAll();

        verify(movieDao, times(1)).getAll();

        assertNotNull(actualMoviesFromStorage);
        assertEquals(3, actualMoviesFromStorage.size());
        assertEquals(thirdMovie.getId(), actualMoviesFromStorage.get(2).getId());
        assertEquals(thirdMovie, actualMoviesFromStorage.get(2));
    }

    @Test
    public void getMovieByIdOk() {
        Long expectedMovieId = 1L;
        when(movieDao.getById(expectedMovieId)).thenReturn(movieStorage.stream()
                .filter(movie -> movie.getId().equals(expectedMovieId))
                .findFirst()
                .orElse(null));

        Movie actualMovieById = movieService.getById(expectedMovieId);

        verify(movieDao, times(1)).getById(any());

        assertEquals(expectedMovieId, actualMovieById.getId());
        assertEquals(firstMovie, actualMovieById);
    }

    @Test
    public void getMovieWithNonexistentId() {
        Long testNonexistentMovieId = 5L;
        when(movieDao.getById(testNonexistentMovieId)).thenReturn(movieStorage.stream()
                .filter(movie -> movie.getId().equals(testNonexistentMovieId))
                .findFirst()
                .orElse(null));

        Movie actualMovieById = movieService.getById(testNonexistentMovieId);

        verify(movieDao, times(1)).getById(any());

        assertNull(actualMovieById);
    }
}
