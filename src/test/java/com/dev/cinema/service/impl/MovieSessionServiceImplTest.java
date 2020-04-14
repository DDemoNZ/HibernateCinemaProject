package com.dev.cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.dao.MovieSessionDao;
import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.MovieSession;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MovieSessionServiceImplTest {

    private static List<MovieSession> movieSessionStorage;
    private static MovieSession testMovieSession;
    private static MovieSession expectedMovieSession;
    private static MovieSession testAvailableMovieSession;

    @InjectMocks
    private MovieSessionServiceImpl movieSessionService;

    @Mock
    private MovieSessionDao movieSessionDao;

    @BeforeAll
    public static void beforeAll() {
        Movie testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Test");
        testMovie.setDescription("Test");

        CinemaHall testCinemaHall = new CinemaHall();
        testCinemaHall.setId(1L);
        testCinemaHall.setCapacity(10);
        testCinemaHall.setDescription("Test");

        expectedMovieSession = new MovieSession();
        expectedMovieSession.setId(1L);
        expectedMovieSession.setMovie(testMovie);
        expectedMovieSession.setCinemaHall(testCinemaHall);
        expectedMovieSession.setShowTime(LocalDateTime.parse("2020-04-10T20:00:00"));

        testMovieSession = new MovieSession();
        testMovieSession.setCinemaHall(testCinemaHall);
        testMovieSession.setMovie(testMovie);
        testMovieSession.setShowTime(expectedMovieSession.getShowTime());

        testAvailableMovieSession = new MovieSession();
        testAvailableMovieSession.setId(2L);
        testAvailableMovieSession.setMovie(testMovie);
        testAvailableMovieSession.setCinemaHall(testCinemaHall);
        testAvailableMovieSession.setShowTime(LocalDateTime.parse("2020-10-10T20:00:00"));

        MovieSession secondTestMovieSession = new MovieSession();
        secondTestMovieSession.setId(1L);
        secondTestMovieSession.setCinemaHall(testCinemaHall);
        secondTestMovieSession.setMovie(testMovie);
        secondTestMovieSession.setShowTime(expectedMovieSession.getShowTime());

        movieSessionStorage = List.of(secondTestMovieSession, testAvailableMovieSession);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addMovieSessionOk() {
        when(movieSessionDao.add(any())).thenReturn(expectedMovieSession);

        MovieSession actualAddedMovieSession = movieSessionService.add(testMovieSession);

        verify(movieSessionDao, times(1)).add(any());
        assertEquals(expectedMovieSession, actualAddedMovieSession);
    }

    @Test
    public void findAvailableSessionsOk() {
        Long expectedMovieId = 1L;
        LocalDateTime expectedDate = LocalDateTime.parse("2020-10-10T20:00");
        when(movieSessionDao.findAvailableSessions(expectedMovieId, expectedDate))
                .thenReturn(movieSessionStorage.stream()
                        .filter(movieSession -> movieSession.getShowTime().equals(expectedDate)
                                && movieSession.getMovie().getId().equals(expectedMovieId))
                        .collect(Collectors.toList()));

        List<MovieSession> actualAvailableSessionsList = movieSessionService.findAvailableSessions(expectedMovieId, expectedDate);

        verify(movieSessionDao, times(1)).findAvailableSessions(anyLong(), any());
        assertEquals(testAvailableMovieSession, actualAvailableSessionsList.get(0));
    }

    @Test
    public void findAvailableSessionsWithNonexistentTime() {
        Long expectedMovieId = 5L;
        LocalDateTime expectedDate = LocalDateTime.parse("2020-11-10T20:00:00");
        when(movieSessionDao.findAvailableSessions(expectedMovieId, expectedDate))
                .thenReturn(movieSessionStorage.stream()
                        .filter(movieSession -> movieSession.getShowTime().equals(expectedDate)
                                && movieSession.getMovie().getId().equals(expectedMovieId))
                        .collect(Collectors.toList()));

        List<MovieSession> actualAvailableSessionsList = movieSessionService.findAvailableSessions(expectedMovieId, expectedDate);

        verify(movieSessionDao, times(1)).findAvailableSessions(anyLong(), any());
        assertEquals(Collections.emptyList(), actualAvailableSessionsList);
    }

    @Test
    public void getMovieSessionByIdOk() {
        Long expectedMovieSessionId = 1L;

        when(movieSessionDao.getById(expectedMovieSessionId)).thenReturn(movieSessionStorage.stream()
                .filter(movieSession -> movieSession.getId().equals(expectedMovieSessionId))
                .findFirst()
                .orElse(null));

        MovieSession actualMovieSessionGetById = movieSessionService.getById(expectedMovieSessionId);

        verify(movieSessionDao, times(1)).getById(anyLong());
        assertNotNull(actualMovieSessionGetById);
        assertEquals(expectedMovieSession.getId(), actualMovieSessionGetById.getId());
        assertEquals(expectedMovieSession, actualMovieSessionGetById);
    }

    @Test
    public void getMovieSessionByNonexistentId() {
        Long expectedMovieSessionId = 5L;

        when(movieSessionDao.getById(expectedMovieSessionId)).thenReturn(movieSessionStorage.stream()
                .filter(movieSession -> movieSession.getId().equals(expectedMovieSessionId))
                .findFirst()
                .orElse(null));

        MovieSession actualMovieSession = movieSessionService.getById(expectedMovieSessionId);

        verify(movieSessionDao, times(1)).getById(anyLong());
        assertNull(actualMovieSession);
    }
}
