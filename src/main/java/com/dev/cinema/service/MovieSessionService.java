package com.dev.cinema.service;

import com.dev.cinema.model.MovieSession;

import java.time.LocalDate;
import java.util.List;

public interface MovieSessionService {

    List<MovieSession> findAvailableSessions(Long movieId, LocalDate date);

    MovieSession add(MovieSession session);
}
