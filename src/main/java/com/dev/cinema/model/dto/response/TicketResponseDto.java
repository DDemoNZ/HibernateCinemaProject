package com.dev.cinema.model.dto.response;

import java.util.Objects;

public class TicketResponseDto {

    private Long ticketId;
    private Long movieSessionId;
    private Long userId;

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TicketResponseDto that = (TicketResponseDto) o;
        return Objects.equals(ticketId, that.ticketId)
                && Objects.equals(movieSessionId, that.movieSessionId)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, movieSessionId, userId);
    }
}
