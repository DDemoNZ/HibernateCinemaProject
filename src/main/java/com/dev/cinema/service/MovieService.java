package com.dev.cinema.service;

import com.dev.cinema.model.Movie;
import java.util.List;

public interface MovieService {

    Movie add(Movie movie);

    List<Movie> getAll();

    Movie getById(Long id);

    Movie update(Movie movie);
}
