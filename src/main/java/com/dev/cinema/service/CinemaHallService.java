package com.dev.cinema.service;

import com.dev.cinema.model.CinemaHall;

import java.util.List;

public interface CinemaHallService {

    CinemaHall add(CinemaHall cinemaHall);

    List<CinemaHall> getAll();
}
