package com.dev.cinema.model.dto.request;

import javax.validation.constraints.Min;

public class ShoppingCartRequestDto {

    @Min(value = 1, message = "Incorrect user_id")
    private Long userId;
    @Min(value = 1, message = "Incorrect movie_session_id")
    private Long movieSessionId;

    public Long getMovieSessionId() {
        return movieSessionId;
    }

    public void setMovieSessionId(Long movieSessionId) {
        this.movieSessionId = movieSessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
