package com.dev.cinema.dao;

import com.dev.cinema.model.MovieSession;

import java.time.LocalDate;
import java.util.List;

public interface MovieSessionDao {

    List<MovieSession> findAvailableSessions(Long movieId, LocalDate date);

    MovieSession add(MovieSession session);

    MovieSession getById(Long id);
}
