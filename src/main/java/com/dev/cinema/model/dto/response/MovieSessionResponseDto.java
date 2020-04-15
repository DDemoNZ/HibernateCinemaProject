package com.dev.cinema.model.dto.response;

import java.util.Objects;

public class MovieSessionResponseDto {

    private Long movieId;
    private Long cinemaHallId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovieSessionResponseDto that = (MovieSessionResponseDto) o;
        return Objects.equals(movieId, that.movieId)
                && Objects.equals(cinemaHallId, that.cinemaHallId)
                && Objects.equals(showTime, that.showTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, cinemaHallId, showTime);
    }
}
