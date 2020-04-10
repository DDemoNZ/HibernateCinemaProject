package com.dev.cinema.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.Role;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.User;
import com.dev.cinema.model.dto.response.ShoppingCartResponseDto;
import com.dev.cinema.service.impl.MovieSessionServiceImpl;
import com.dev.cinema.service.impl.ShoppingCartServiceImpl;
import com.dev.cinema.service.impl.UserServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

class ShoppingCartControllerTest {

    private static List<MovieSession> mockMovieSessionStorage;
    private static MovieSession expectedSecondMovieSession;
    private static User secondMockUser;
    private static ShoppingCart mockSecondUserShoppingCart;
    private static ShoppingCart expectedSecondUserShoppingCart;
    private static List<User> mockUserStorage;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private MovieSessionServiceImpl movieSessionService;

    @Mock
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    @BeforeAll
    static void beforeAll() {
        CinemaHall expectedFirstCinemaHall = new CinemaHall();
        expectedFirstCinemaHall.setId(1L);
        expectedFirstCinemaHall.setCapacity(20);
        expectedFirstCinemaHall.setDescription("FirstCinemaHall");

        CinemaHall expectedSecondCinemaHall = new CinemaHall();
        expectedSecondCinemaHall.setId(2L);
        expectedSecondCinemaHall.setCapacity(30);
        expectedSecondCinemaHall.setDescription("SecondCinemaHall");

        Movie expectedFirstMovie = new Movie();
        expectedFirstMovie.setId(1L);
        expectedFirstMovie.setTitle("FirstTitle");
        expectedFirstMovie.setDescription("FirstDescription");

        Movie expectedSecondMovie = new Movie();
        expectedSecondMovie.setId(2L);
        expectedSecondMovie.setTitle("SecondTitle");
        expectedSecondMovie.setDescription("SecondDescription");

        MovieSession expectedFirstMovieSession = new MovieSession();
        expectedFirstMovieSession.setId(1L);
        expectedFirstMovieSession.setShowTime(LocalDateTime.parse("2020-04-11T20:00:00"));
        expectedFirstMovieSession.setCinemaHall(expectedFirstCinemaHall);
        expectedFirstMovieSession.setMovie(expectedFirstMovie);

        expectedSecondMovieSession = new MovieSession();
        expectedSecondMovieSession.setId(2L);
        expectedSecondMovieSession.setShowTime(LocalDateTime.parse("2020-04-10T15:00:00"));
        expectedSecondMovieSession.setCinemaHall(expectedSecondCinemaHall);
        expectedSecondMovieSession.setMovie(expectedFirstMovie);

        MovieSession expectedThirdMovieSession = new MovieSession();
        expectedThirdMovieSession.setId(3L);
        expectedThirdMovieSession.setShowTime(LocalDateTime.parse("2020-04-10T20:00:00"));
        expectedThirdMovieSession.setCinemaHall(expectedSecondCinemaHall);
        expectedThirdMovieSession.setMovie(expectedSecondMovie);

        mockMovieSessionStorage = new ArrayList<>();
        mockMovieSessionStorage.add(expectedFirstMovieSession);
        mockMovieSessionStorage.add(expectedSecondMovieSession);
        mockMovieSessionStorage.add(expectedThirdMovieSession);

        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setRoleName("USER");

        User firstMockUser = new User();
        firstMockUser.setId(1L);
        firstMockUser.setEmail("FirstUserEmail");
        firstMockUser.setPassword("SecondUserPassword");
        firstMockUser.getRoles().add(userRole);

        secondMockUser = new User();
        secondMockUser.setId(2L);
        secondMockUser.setEmail("SecondUserEmail");
        secondMockUser.setPassword("SecondUserPassword");
        secondMockUser.getRoles().add(userRole);

        mockUserStorage = new ArrayList<>();
        mockUserStorage.add(firstMockUser);
        mockUserStorage.add(secondMockUser);

        Ticket mockTicketForSecondMovieSecondSession = new Ticket();
        mockTicketForSecondMovieSecondSession.setUser(secondMockUser);
        mockTicketForSecondMovieSecondSession.setMovieSession(expectedSecondMovieSession);
        mockTicketForSecondMovieSecondSession.setId(1L);

        expectedSecondUserShoppingCart = new ShoppingCart();
        expectedSecondUserShoppingCart.setId(1L);
        expectedSecondUserShoppingCart.setUser(secondMockUser);
        expectedSecondUserShoppingCart.getTickets().add(mockTicketForSecondMovieSecondSession);

        mockSecondUserShoppingCart = new ShoppingCart();
        mockSecondUserShoppingCart.setId(1L);
        mockSecondUserShoppingCart.setUser(secondMockUser);
        mockSecondUserShoppingCart.getTickets().add(mockTicketForSecondMovieSecondSession);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addMovieSessionAllOk() {
        when(authentication.getName()).thenReturn(secondMockUser.getEmail());

        Long expectedMovieSessionId = 2L;
        when(movieSessionService.getById(expectedMovieSessionId))
                .thenReturn(mockMovieSessionStorage.stream()
                        .filter(movieSession ->
                                movieSession.getId().equals(expectedMovieSessionId))
                        .findFirst()
                        .orElse(null));

        String expectedUserEmail = "SecondUserEmail";
        when(userService.findByEmail(expectedUserEmail)).thenReturn(mockUserStorage.stream()
                .filter(user -> user.getEmail().equals(expectedUserEmail))
                .findFirst()
                .orElse(null));

        when(shoppingCartService.getByUser(secondMockUser)).thenReturn(mockSecondUserShoppingCart);

        ShoppingCartResponseDto actualShoppingCartResponseDto =
                shoppingCartController.addMovieSession(expectedMovieSessionId, authentication);

        verify(shoppingCartService, times(1)).addSession(expectedSecondMovieSession, secondMockUser);
        verify(shoppingCartService, times(1)).getByUser(any());
        verify(movieSessionService, times(1)).getById(anyLong());
        verify(userService, times(1)).findByEmail(anyString());

        assertNotNull(actualShoppingCartResponseDto);
        assertEquals(mockSecondUserShoppingCart.getUser().getId(),
                actualShoppingCartResponseDto.getUserId());
        assertEquals(mockSecondUserShoppingCart.getTickets().get(0).getMovieSession().getId(),
                actualShoppingCartResponseDto.getTickets().get(0).getMovieSessionId());
        assertEquals(mockSecondUserShoppingCart.getTickets().get(0).getId(),
                actualShoppingCartResponseDto.getTickets().get(0).getTicketId());
        assertEquals(mockSecondUserShoppingCart.getTickets().get(0).getUser().getId(),
                actualShoppingCartResponseDto.getTickets().get(0).getUserId());
    }

    @Test
    void getShoppingCarByUserIdOk() {
        when(authentication.getName()).thenReturn(secondMockUser.getEmail());
        when(shoppingCartService.getByUser(any())).thenReturn(mockSecondUserShoppingCart);
        when(userService.findByEmail(any())).thenReturn(secondMockUser);

        ShoppingCartResponseDto actualShoppingCarByUserIdResponseDto =
                shoppingCartController.getShoppingCarByUserId(authentication);

        verify(shoppingCartService, times(1)).getByUser(any());
        verify(userService, times(1)).findByEmail(any());
        assertEquals(expectedSecondUserShoppingCart.getTickets().get(0).getMovieSession().getId(),
                actualShoppingCarByUserIdResponseDto.getTickets().get(0).getMovieSessionId());
        assertEquals(expectedSecondUserShoppingCart.getUser().getId(),
                actualShoppingCarByUserIdResponseDto.getUserId());
    }

    @Test
    void getShoppingCarByNonexistentUserId() {
        when(authentication.getName()).thenReturn("Nonexistent");
        when(shoppingCartService.getByUser(any())).thenReturn(null);
        when(userService.findByEmail(any())).thenReturn(null);

        verify(shoppingCartService, times(0)).getByUser(any());
        verify(userService, times(0)).findByEmail(any());
        assertThrows(Exception.class,
                () -> shoppingCartController.getShoppingCarByUserId(authentication));
    }
}