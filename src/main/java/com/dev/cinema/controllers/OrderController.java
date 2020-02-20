package com.dev.cinema.controllers;

import com.dev.cinema.model.Orders;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.dto.response.OrdersResponseDto;
import com.dev.cinema.model.dto.response.TicketResponseDto;
import com.dev.cinema.service.OrderService;
import com.dev.cinema.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/complete")
    public OrdersResponseDto completeOrder(@RequestParam Long userId) {
        Orders orders = orderService.completeOrder(userService.getById(userId));

        return getOrdersResponseDto(orders);
    }

    @GetMapping("/order_history/{user_id}")
    public List<OrdersResponseDto> getOrdersHistory(@PathVariable("user_id") Long userId) {
        List<Orders> orderHistory = orderService.getOrderHistory(userService.getById(userId));
        return orderHistory.stream().map(this::getOrdersResponseDto)
                .collect(Collectors.toList());
    }

    private OrdersResponseDto getOrdersResponseDto(Orders orders) {
        OrdersResponseDto ordersResponseDto = new OrdersResponseDto();
        ordersResponseDto.setOrderId(orders.getId());
        ordersResponseDto.setTickets(getListTicketsResponseDto(orders));
        ordersResponseDto.setUserId(orders.getUser().getId());
        return ordersResponseDto;
    }

    private List<TicketResponseDto> getListTicketsResponseDto(Orders orders) {
        List<Ticket> tickets = orders.getTickets();
        return tickets.stream()
                .map(this::getTicketResponseDto)
                .collect(Collectors.toList());
    }

    private TicketResponseDto getTicketResponseDto(Ticket ticket) {
        TicketResponseDto ticketResponseDto = new TicketResponseDto();
        ticketResponseDto.setTicketId(ticket.getId());
        ticketResponseDto.setMovieSessionId(ticket.getMovieSession().getId());
        ticketResponseDto.setUserId(ticket.getUser().getId());
        return ticketResponseDto;
    }
}
