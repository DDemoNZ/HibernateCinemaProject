package com.dev.cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.dao.ShoppingCartDao;
import com.dev.cinema.dao.TicketDao;
import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ShoppingCartServiceImplTest {

    private static ShoppingCart expectedMockShoppingCart;
    private static ShoppingCart expectedAfterUpdateMockShoppingCart;
    private static Ticket expectedTicket;
    private static MovieSession mockMovieSession;
    private static User mockUser;
    private static List<ShoppingCart> mockShoppingCartStorage;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private ShoppingCartDao shoppingCartDao;

    @Mock
    private TicketDao ticketDao;

    @BeforeAll
    public static void beforeAll() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("TestUser");
        mockUser.setPassword("TestPassword");

        mockMovieSession = new MovieSession();

        Movie mockMovie = new Movie();
        mockMovie.setId(1L);
        mockMovie.setTitle("MockMovie");
        mockMovie.setDescription("MockMovie");

        CinemaHall mockCinemaHall = new CinemaHall();
        mockCinemaHall.setId(1L);
        mockCinemaHall.setCapacity(20);
        mockCinemaHall.setDescription("MockCinemaHall");

        mockMovieSession.setId(1L);
        mockMovieSession.setMovie(mockMovie);
        mockMovieSession.setCinemaHall(mockCinemaHall);
        mockMovieSession.setShowTime(LocalDateTime.parse("2020-04-10T20:00:00"));

        expectedMockShoppingCart = new ShoppingCart();
        expectedMockShoppingCart.setId(1L);
        expectedMockShoppingCart.setUser(mockUser);

        expectedTicket = new Ticket();
        expectedTicket.setId(1L);
        expectedTicket.setMovieSession(mockMovieSession);
        expectedTicket.setUser(mockUser);

        expectedAfterUpdateMockShoppingCart = new ShoppingCart();
        expectedAfterUpdateMockShoppingCart.setId(expectedMockShoppingCart.getId());
        expectedAfterUpdateMockShoppingCart.getTickets().add(expectedTicket);
        expectedAfterUpdateMockShoppingCart.setUser(mockUser);

        mockShoppingCartStorage = new ArrayList<>();
        mockShoppingCartStorage.add(expectedAfterUpdateMockShoppingCart);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addSessionOk() {
        when(shoppingCartDao.getByUser(any())).thenReturn(expectedMockShoppingCart);
        when(shoppingCartDao.update(any())).thenReturn(expectedAfterUpdateMockShoppingCart);
        when(ticketDao.add(any())).thenReturn(expectedTicket);
        ShoppingCart actualShoppingCart = shoppingCartService.addSession(mockMovieSession, mockUser);

        verify(shoppingCartDao, times(1)).getByUser(any());
        verify(ticketDao, times(1)).add(any());
        verify(shoppingCartDao, times(1)).update(any());
        assertNotNull(actualShoppingCart);
        assertEquals(expectedAfterUpdateMockShoppingCart.getUser(), actualShoppingCart.getUser());
        assertEquals(expectedTicket, actualShoppingCart.getTickets().get(0));
        assertEquals(expectedMockShoppingCart.getId(), actualShoppingCart.getId());
    }

    @Test
    public void getByUserOk() {
        when(shoppingCartDao.getByUser(mockUser)).thenReturn(mockShoppingCartStorage.stream()
                .filter(shoppingCart -> shoppingCart.getUser().equals(mockUser))
                .findFirst()
                .orElse(null));

        ShoppingCart actualShoppingCartByUser = shoppingCartService.getByUser(mockUser);

        verify(shoppingCartDao, times(1)).getByUser(any());
        assertNotNull(actualShoppingCartByUser);
        assertEquals(expectedMockShoppingCart.getUser(), actualShoppingCartByUser.getUser());
    }

    @Test
    public void getByUserNonexistent() {
        User nonexistentUser = new User();
        when(shoppingCartDao.getByUser(nonexistentUser)).thenReturn(mockShoppingCartStorage.stream()
                .filter(shoppingCart -> shoppingCart.getUser().equals(nonexistentUser))
                .findFirst()
                .orElse(null));

        assertNull(shoppingCartService.getByUser(nonexistentUser));
    }

    @Test
    public void registerNewShoppingCartOk() {
        when(shoppingCartDao.add(any())).thenReturn(expectedMockShoppingCart);

        ShoppingCart actualShoppingCart = shoppingCartService.registerNewShoppingCart(mockUser);

        verify(shoppingCartDao, times(1)).add(any());
        assertNotNull(actualShoppingCart);
        assertEquals(expectedMockShoppingCart.getUser(), actualShoppingCart.getUser());
        assertEquals(expectedMockShoppingCart.getId(), actualShoppingCart.getId());
    }

    @Test
    public void clearOk() {
        ShoppingCart expectedShoppingCart = new ShoppingCart();
        expectedShoppingCart.setId(expectedAfterUpdateMockShoppingCart.getId());
        expectedShoppingCart.setUser(expectedAfterUpdateMockShoppingCart.getUser());
        when(shoppingCartDao.update(any())).thenReturn(expectedShoppingCart);

        ShoppingCart actualShoppingCart = shoppingCartService.clear(expectedAfterUpdateMockShoppingCart);

        verify(shoppingCartDao, times(1)).update(any());
        assertEquals(Collections.emptyList(), actualShoppingCart.getTickets());
        assertEquals(expectedShoppingCart.getTickets(), actualShoppingCart.getTickets());
    }

    @Test
    public void getById() {
        Long expectedId = 1L;
        when(shoppingCartDao.getById(anyLong())).thenReturn(mockShoppingCartStorage.stream()
                .filter(shoppingCart -> shoppingCart.getId().equals(expectedId))
                .findFirst()
                .orElse(null));

        ShoppingCart actualShoppingCartById = shoppingCartService.getById(expectedId);

        verify(shoppingCartDao, times(1)).getById(anyLong());
        assertEquals(expectedId, actualShoppingCartById.getId());
        assertEquals(mockUser, actualShoppingCartById.getUser());
        assertEquals(expectedTicket, actualShoppingCartById.getTickets().get(0));
    }

    @Test
    public void getByNonexistentId() {
        Long expectedId = 2L;
        when(shoppingCartDao.getById(anyLong())).thenReturn(mockShoppingCartStorage.stream()
                .filter(shoppingCart -> shoppingCart.getId().equals(expectedId))
                .findFirst()
                .orElse(null));

        assertNull(shoppingCartService.getById(expectedId));
    }
}
