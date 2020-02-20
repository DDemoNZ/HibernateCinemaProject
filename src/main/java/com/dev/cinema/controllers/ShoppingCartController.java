package com.dev.cinema.controllers;

import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.User;
import com.dev.cinema.model.dto.request.ShoppingCartRequestDto;
import com.dev.cinema.model.dto.response.ShoppingCartResponseDto;
import com.dev.cinema.model.dto.response.TicketResponseDto;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("shopping_cart")
public class ShoppingCartController {

    private final UserService userService;
    private final MovieSessionService movieSessionService;
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  MovieSessionService movieSessionService,
                                  UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.movieSessionService = movieSessionService;
        this.userService = userService;
    }

    @PostMapping("/add_movie_session")
    public ShoppingCartResponseDto addMovieSession(@RequestBody ShoppingCartRequestDto
                                                           shoppingCartRequestDto) {
        MovieSession movieSession = movieSessionService.getById(shoppingCartRequestDto
                .getMovieSessionId());
        User user = userService.getById(shoppingCartRequestDto.getUserId());
        shoppingCartService.addSession(movieSession, user);
        return getShoppingCartResponseDto(shoppingCartService.getByUser(user));
    }

    @GetMapping("/get_by_id")
    public ShoppingCartResponseDto getShoppingCarByUserId(@RequestParam Long userId) {
        return getShoppingCartResponseDto(shoppingCartService
                .getByUser(userService.getById(userId)));
    }

    private ShoppingCartResponseDto getShoppingCartResponseDto(ShoppingCart shoppingCart) {
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setTickets(getListTicketsResponseDto(shoppingCart));
        shoppingCartResponseDto.setUserId(shoppingCart.getUser().getId());
        return shoppingCartResponseDto;
    }

    private List<TicketResponseDto> getListTicketsResponseDto(ShoppingCart shoppingCart) {
        List<Ticket> tickets = shoppingCart.getTickets();
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
