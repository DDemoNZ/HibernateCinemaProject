package com.dev.cinema.model.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class MovieSessionRequestDto {

    @Min(value = 1, message = "Incorrect movie_id")
    private Long movieId;
    @Min(value = 1, message = "Incorrect cinema_hall_id")
    private Long cinemaHallId;
    @NotEmpty(message = "Show_time must not be empty")
    private String showTime;

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getCinemaHallId() {
        return cinemaHallId;
    }

    public void setCinemaHallId(Long cinemaHallId) {
        this.cinemaHallId = cinemaHallId;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }
}
