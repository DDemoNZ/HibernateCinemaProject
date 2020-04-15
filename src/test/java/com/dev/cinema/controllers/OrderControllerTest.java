package com.dev.cinema.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.Orders;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.User;
import com.dev.cinema.model.dto.request.UserRequestDto;
import com.dev.cinema.model.dto.response.OrdersResponseDto;
import com.dev.cinema.model.dto.response.TicketResponseDto;
import com.dev.cinema.service.impl.OrderServiceImpl;
import com.dev.cinema.service.impl.UserServiceImpl;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

public class OrderControllerTest {

    private static User mockTestUser;
    private static Orders expectedOrder;
    private static OrdersResponseDto expectedOrderResponseDto;
    private static TicketResponseDto testTicketResponseDto;
    private static UserRequestDto testMockUserRequestDto;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private Authentication authentication;

    @BeforeAll
    public static void beforeAll() {
        mockTestUser = new User();
        mockTestUser.setId(1L);
        mockTestUser.setEmail("TestUser");
        mockTestUser.setPassword("TestUser");

        Movie testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("TestMovie");
        testMovie.setDescription("TestMovie");

        CinemaHall testCinemaHall = new CinemaHall();
        testCinemaHall.setId(1L);
        testCinemaHall.setCapacity(10);
        testCinemaHall.setDescription("TestCinemaHall");

        MovieSession testMovieSession = new MovieSession();
        testMovieSession.setId(1L);
        testMovieSession.setCinemaHall(testCinemaHall);
        testMovieSession.setMovie(testMovie);

        Ticket testTicket = new Ticket();
        testTicket.setId(1L);
        testTicket.setUser(mockTestUser);
        testTicket.setMovieSession(testMovieSession);

        expectedOrder = new Orders();
        expectedOrder.setId(1L);
        expectedOrder.getTickets().add(testTicket);
        expectedOrder.setUser(mockTestUser);

        testTicketResponseDto = new TicketResponseDto();
        testTicketResponseDto.setUserId(mockTestUser.getId());
        testTicketResponseDto.setTicketId(1L);
        testTicketResponseDto.setMovieSessionId(testMovieSession.getId());
        testTicketResponseDto.setMovieSessionId(testMovieSession.getId());

        expectedOrderResponseDto = new OrdersResponseDto();
        expectedOrderResponseDto.getTickets().add(testTicketResponseDto);
        expectedOrderResponseDto.setOrderId(expectedOrder.getId());
        expectedOrderResponseDto.setUserId(mockTestUser.getId());

        testMockUserRequestDto = new UserRequestDto();
        testMockUserRequestDto.setEmail("TestUser");
        testMockUserRequestDto.setPassword("TestUser");
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void completeOrder() {
        OrdersResponseDto expectedOrderResponseDto = new OrdersResponseDto();
        expectedOrderResponseDto.setOrderId(expectedOrder.getId());
        expectedOrderResponseDto.getTickets().add(testTicketResponseDto);
        expectedOrderResponseDto.setUserId(mockTestUser.getId());

        when(authentication.getName()).thenReturn(mockTestUser.getEmail());
        when(orderService.completeOrder(mockTestUser)).thenReturn(expectedOrder);
        when(userService.findByEmail(anyString())).thenReturn(mockTestUser);

        OrdersResponseDto actualOrdersResponseDto = orderController.completeOrder(1L, authentication);

        verify(orderService, times(1)).completeOrder(any());
        verify(userService, times(1)).findByEmail(anyString());
        assertEquals(expectedOrderResponseDto, actualOrdersResponseDto);

    }

    @Test
    public void completeOrderWithoutTickets() {
        when(authentication.getName()).thenReturn(mockTestUser.getEmail());
        when(orderService.completeOrder(mockTestUser)).thenReturn(expectedOrder);
        when(userService.findByEmail(anyString())).thenReturn(mockTestUser);

        OrdersResponseDto actualOrdersResponseDto = orderController.completeOrder(1L, authentication);

        verify(orderService, times(1)).completeOrder(any());
        verify(userService, times(1)).findByEmail(anyString());
        assertEquals(expectedOrderResponseDto, actualOrdersResponseDto);
    }

    @Test
    public void getUserOrdersHistoryOk() {
        when(orderService.getOrderHistory(mockTestUser)).thenReturn(List.of(expectedOrder));
        when(userService.findByEmail(anyString())).thenReturn(mockTestUser);

        List<OrdersResponseDto> actualOrdersHistoryList =
                orderController.getOrdersHistory(testMockUserRequestDto);

        verify(orderService, times(1)).getOrderHistory(any());
        verify(userService, times(1)).findByEmail(anyString());
        assertEquals(List.of(expectedOrderResponseDto), actualOrdersHistoryList);
    }

    @Test
    public void getOrdersHistoryWithoutOrdersOk() {
        when(orderService.getOrderHistory(mockTestUser)).thenReturn(Collections.emptyList());
        when(userService.findByEmail(anyString())).thenReturn(mockTestUser);

        List<OrdersResponseDto> actualOrdersHistoryWithoutOrders =
                orderController.getOrdersHistory(testMockUserRequestDto);

        verify(orderService, times(1)).getOrderHistory(any());
        verify(userService, times(1)).findByEmail(anyString());
        assertEquals(Collections.emptyList(), actualOrdersHistoryWithoutOrders);
        assertEquals(0, actualOrdersHistoryWithoutOrders.size());
    }
}
