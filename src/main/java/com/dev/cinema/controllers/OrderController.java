package com.dev.cinema.controllers;

import com.dev.cinema.model.Orders;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.dto.request.UserRequestDto;
import com.dev.cinema.model.dto.response.OrdersResponseDto;
import com.dev.cinema.model.dto.response.TicketResponseDto;
import com.dev.cinema.service.OrderService;
import com.dev.cinema.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/order")
    public OrdersResponseDto completeOrder(@RequestParam @Valid Long userId,
                                           @Valid Authentication authentication) {
        Orders orders = orderService.completeOrder(userService
                .findByEmail(authentication.getName()));
        return getOrdersResponseDto(orders);
    }

    @GetMapping("/order-history")
    public List<OrdersResponseDto> getOrdersHistory(@RequestParam @Valid UserRequestDto
                                                            userRequestDto) {
        List<Orders> orderHistory =
                orderService.getOrderHistory(userService.findByEmail(userRequestDto.getEmail()));
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
