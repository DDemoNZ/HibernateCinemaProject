package com.dev.cinema.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.dto.request.MovieSessionRequestDto;
import com.dev.cinema.model.dto.response.MovieSessionResponseDto;
import com.dev.cinema.service.impl.CinemaHallServiceImpl;
import com.dev.cinema.service.impl.MovieServiceImpl;
import com.dev.cinema.service.impl.MovieSessionServiceImpl;
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

class MovieSessionControllerTest {

    private static MovieSession expectedMockMovieSession;
    private static Movie expectedMockMovieForMockMovieSession;
    private static CinemaHall expectedMockCinemaHallForMockMovieSession;
    private static MovieSessionResponseDto expectedMovieSessionResponseDto;
    private static MovieSessionRequestDto movieSessionRequestDto;

    @InjectMocks
    private MovieSessionController movieSessionController;

    @Mock
    private MovieSessionServiceImpl movieSessionService;

    @Mock
    private MovieServiceImpl movieService;

    @Mock
    private CinemaHallServiceImpl cinemaHallService;

    @BeforeAll
    static void beforeAll() {
        expectedMockMovieForMockMovieSession = new Movie();
        expectedMockMovieForMockMovieSession.setId(1L);
        expectedMockMovieForMockMovieSession.setTitle("Test");
        expectedMockMovieForMockMovieSession.setDescription("Test");

        expectedMockCinemaHallForMockMovieSession = new CinemaHall();
        expectedMockCinemaHallForMockMovieSession.setId(1L);
        expectedMockCinemaHallForMockMovieSession.setCapacity(10);
        expectedMockCinemaHallForMockMovieSession.setDescription("Test");

        expectedMockMovieSession = new MovieSession();
        expectedMockMovieSession.setId(1L);
        expectedMockMovieSession.setMovie(expectedMockMovieForMockMovieSession);
        expectedMockMovieSession.setCinemaHall(expectedMockCinemaHallForMockMovieSession);
        expectedMockMovieSession.setShowTime(LocalDateTime.parse("2020-04-10T15:00"));

        movieSessionRequestDto = new MovieSessionRequestDto();
        movieSessionRequestDto.setMovieId(1L);
        movieSessionRequestDto.setCinemaHallId(1L);
        movieSessionRequestDto.setShowTime("2020-04-10T15:00");

        expectedMovieSessionResponseDto = new MovieSessionResponseDto();
        expectedMovieSessionResponseDto.setCinemaHallId(movieSessionRequestDto.getCinemaHallId());
        expectedMovieSessionResponseDto.setMovieId(movieSessionRequestDto.getMovieId());
        expectedMovieSessionResponseDto.setShowTime(movieSessionRequestDto.getShowTime());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addMovieSessionOk() {
        when(movieService.getById(anyLong())).thenReturn(expectedMockMovieForMockMovieSession);
        when(cinemaHallService.getById(anyLong())).thenReturn(expectedMockCinemaHallForMockMovieSession);
        when(movieSessionService.add(any())).thenReturn(expectedMockMovieSession);

        MovieSessionResponseDto actualMovieSessionResponseDto = movieSessionController.addMovieSession(movieSessionRequestDto);

        verify(movieService, times(1)).getById(anyLong());
        verify(cinemaHallService, times(1)).getById(anyLong());
        verify(movieSessionService, times(1)).add(any());
        assertEquals(expectedMovieSessionResponseDto, actualMovieSessionResponseDto);
        assertEquals(expectedMovieSessionResponseDto.getMovieId(),
                actualMovieSessionResponseDto.getMovieId());
    }

    @Test
    void getAllAvailableSessionsOk() {
        List<MovieSession> expectedAvailableMovieSessionStorage = List.of(expectedMockMovieSession);
        List<MovieSessionResponseDto> expectedMovieSessionResponseDtoList =
                List.of(expectedMovieSessionResponseDto);
        String expectedDate = "2020-04-10T15:00";
        Long expectedMovieId = 1L;

        when(movieSessionService.findAvailableSessions(expectedMovieId,
                LocalDateTime.parse(expectedDate))).thenReturn(expectedAvailableMovieSessionStorage.stream()
                .filter(movieSession -> movieSession.getMovie().getId().equals(expectedMovieId)
                        && movieSession.getShowTime().equals(LocalDateTime.parse(expectedDate))).collect(Collectors.toList()));

        List<MovieSessionResponseDto> actualAllAvailableSessionsList = movieSessionController.getAllAvailableSessions(expectedMovieId,
                expectedDate);

        verify(movieSessionService, times(1)).findAvailableSessions(anyLong(), any());
        assertNotNull(actualAllAvailableSessionsList);
        assertEquals(expectedMovieSessionResponseDtoList.get(0),
                actualAllAvailableSessionsList.get(0));
    }

    @Test
    void getAllAvailableSessionsWithNoAvailable() {
        List<MovieSession> expectedAvailableMovieSessionStorage = Collections.emptyList();
        List<MovieSessionResponseDto> expectedMovieSessionResponseDtoList = Collections.emptyList();
        LocalDateTime expectedDate = LocalDateTime.parse("2020-04-10T15:00");
        Long expectedMovieId = 1L;

        when(movieSessionService.findAvailableSessions(expectedMovieId,
                expectedDate)).thenReturn(expectedAvailableMovieSessionStorage.stream()
                .filter(movieSession -> movieSession.getMovie().getId().equals(expectedMovieId)
                        && movieSession.getShowTime().equals(expectedDate)).collect(Collectors.toList()));

        List<MovieSessionResponseDto> actualAllAvailableSessionsList = movieSessionController.getAllAvailableSessions(expectedMovieId,
                String.valueOf(expectedDate));

        verify(movieSessionService, times(1)).findAvailableSessions(anyLong(), any());
        assertNotNull(actualAllAvailableSessionsList);
        assertEquals(expectedMovieSessionResponseDtoList.size(), actualAllAvailableSessionsList.size());
    }
}