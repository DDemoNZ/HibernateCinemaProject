package com.dev.cinema.dao;

import com.dev.cinema.model.MovieSession;
import java.time.LocalDateTime;
import java.util.List;

public interface MovieSessionDao {

    List<MovieSession> findAvailableSessions(Long movieId, LocalDateTime date);

    MovieSession add(MovieSession session);

    MovieSession getById(Long id);
}
