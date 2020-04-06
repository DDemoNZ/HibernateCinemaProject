package com.dev.cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.dao.MovieDao;
import com.dev.cinema.model.Movie;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MovieServiceImplTest {

    @InjectMocks
    private MovieServiceImpl movieService;

    @Mock
    private MovieDao movieDao;

    private Movie first;
    private Movie second;
    private Movie third;
    private List<Movie> movieStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addMovieOk() {
        Movie mockMovie = new Movie();
        mockMovie.setId(1L);
        mockMovie.setTitle("Test");
        mockMovie.setDescription("Test");

        Movie testMovie = new Movie();
        testMovie.setTitle("Test");
        testMovie.setDescription("Test");

        when(movieDao.add(testMovie)).thenReturn(mockMovie);

        Movie added = movieService.add(testMovie);

        assertNotNull(added.getId());
        assertEquals("Test", added.getTitle());
    }

    @Test
    void getAllOk() {
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

        movieStorage = new ArrayList<>();
        movieStorage.add(first);
        movieStorage.add(second);
        movieStorage.add(third);

        when(movieDao.getAll()).thenReturn(movieStorage);

        List<Movie> movies = movieService.getAll();

        verify(movieDao, times(1)).getAll();

        assertNotNull(movies);
        assertEquals(3, movies.size());
        assertEquals(third.getId(), movies.get(2).getId());
        assertEquals(third, movies.get(2));
    }

    @Test
    void getByIdOk() {
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

        movieStorage = new ArrayList<>();
        movieStorage.add(first);
        movieStorage.add(second);
        movieStorage.add(third);

        when(movieDao.getById(1L)).thenReturn(movieStorage.get(0));

        Movie byId = movieService.getById(1L);

        verify(movieDao, times(1)).getById(any());
        assertEquals(first, byId);
    }
}
