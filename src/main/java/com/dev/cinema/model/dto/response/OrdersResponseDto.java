package com.dev.cinema.model.dto.response;

import java.util.List;

public class OrdersResponseDto {

    private Long userId;
    private List<TicketResponseDto> tickets;

    public List<TicketResponseDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketResponseDto> tickets) {
        this.tickets = tickets;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
