package com.dev.cinema.model.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdersResponseDto {

    private Long orderId;
    private Long userId;
    private List<TicketResponseDto> tickets = new ArrayList<>();

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrdersResponseDto that = (OrdersResponseDto) o;
        return Objects.equals(orderId, that.orderId)
                && Objects.equals(userId, that.userId)
                && Objects.equals(tickets, that.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, userId, tickets);
    }
}
