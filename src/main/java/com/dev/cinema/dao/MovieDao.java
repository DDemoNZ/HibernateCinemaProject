package com.dev.cinema.dao;

import com.dev.cinema.model.Movie;
import java.util.List;

public interface MovieDao {

    Movie add(Movie movie);

    List<Movie> getAll();

    Movie getById(Long id);

    Movie update(Movie movie);
}
