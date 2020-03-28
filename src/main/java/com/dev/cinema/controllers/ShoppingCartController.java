package com.dev.cinema.controllers;

import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.User;
import com.dev.cinema.model.dto.response.ShoppingCartResponseDto;
import com.dev.cinema.model.dto.response.TicketResponseDto;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.ShoppingCartService;
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
@RequestMapping("/shopping-cart")
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

    @PostMapping("/add-movie-session")
    public ShoppingCartResponseDto addMovieSession(@RequestParam Long movieSessionId,
                                                   @Valid Authentication authentication) {
        MovieSession movieSession = movieSessionService.getById(movieSessionId);
        User user = userService.findByEmail(authentication.getName());
        shoppingCartService.addSession(movieSession, user);
        return getShoppingCartResponseDto(shoppingCartService.getByUser(user));
    }

    @GetMapping("/by-user-id")
    public ShoppingCartResponseDto getShoppingCarByUserId(@Valid Authentication authentication) {
        return getShoppingCartResponseDto(shoppingCartService
                .getByUser(userService.findByEmail(authentication.getName())));
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
