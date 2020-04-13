package com.dev.cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.dao.OrderDao;
import com.dev.cinema.model.Orders;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OrderServiceImplTest {

    private static User expectedUser;
    private static List<Orders> ordersStorage;
    private static Orders expectedOrder;
    private static ShoppingCart expectedShoppingCartByUser;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeAll
    static void beforeAll() {
        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail("Test");
        expectedUser.setPassword("Test");

        ordersStorage = new ArrayList<>();

        expectedShoppingCartByUser = new ShoppingCart();
        expectedShoppingCartByUser.setUser(expectedUser);
        expectedShoppingCartByUser.setId(1L);

        expectedOrder = new Orders();
        expectedOrder.setId(1L);
        expectedOrder.setUser(expectedUser);
        expectedOrder.setTickets(expectedShoppingCartByUser.getTickets());

        Ticket ticket = new Ticket();
        ticket.setUser(expectedUser);
        ticket.setId(1L);
        expectedShoppingCartByUser.getTickets().add(ticket);

        ordersStorage.add(expectedOrder);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void completeOrderOk() {
        ShoppingCart shoppingCartAfterClear = new ShoppingCart();
        shoppingCartAfterClear.setId(expectedShoppingCartByUser.getId());
        shoppingCartAfterClear.setUser(expectedShoppingCartByUser.getUser());
        shoppingCartAfterClear.setTickets(Collections.emptyList());

        when(shoppingCartService.getByUser(expectedUser)).thenReturn(expectedShoppingCartByUser);
        when(orderDao.add(any())).thenReturn(expectedOrder);
        when(shoppingCartService.clear(any())).thenReturn(shoppingCartAfterClear);

        Orders actualOrder = orderService.completeOrder(expectedUser);

        verify(shoppingCartService, times(1)).getByUser(any());
        verify(shoppingCartService, times(1)).clear(any());
        verify(orderDao, times(1)).add(any());
        assertEquals(expectedOrder.getUser(), actualOrder.getUser());
        assertEquals(expectedOrder.getTickets(), actualOrder.getTickets());
        assertEquals(expectedOrder.getId(), actualOrder.getId());
    }

    @Test
    void getOrderHistoryOk() {
        when(orderDao.getUserOrderHistory(any())).thenReturn(ordersStorage.stream()
                .filter(orders -> orders.getUser().equals(expectedUser))
                .collect(Collectors.toList()));

        List<Orders> actualOrderHistory = orderService.getOrderHistory(expectedUser);

        verify(orderDao, times(1)).getUserOrderHistory(any());
        assertEquals(expectedOrder, actualOrderHistory.get(0));
        assertEquals(expectedUser, actualOrderHistory.get(0).getUser());
    }

    @Test
    void getOrderHistoryWithNonexistentUser() {
        User nonexistentUser = new User();
        nonexistentUser.setId(5L);
        nonexistentUser.setEmail("nonexistent");

        when(orderDao.getUserOrderHistory(any())).thenReturn(ordersStorage.stream()
                .filter(orders -> orders.getUser().equals(nonexistentUser))
                .collect(Collectors.toList()));

        List<Orders> actualOrderHistoryWithNonexistentUser = orderService.getOrderHistory(nonexistentUser);

        verify(orderDao, times(1)).getUserOrderHistory(any());
        assertEquals(Collections.emptyList(), actualOrderHistoryWithNonexistentUser);
    }

    @Test
    void getByOrderIdOk() {
        Long expectedOkId = 1L;
        when(orderDao.getById(anyLong())).thenReturn(ordersStorage.stream()
                .filter(orders -> orders.getId().equals(expectedOkId))
                .findFirst()
                .orElse(null));

        Orders actualOrderByOkId = orderService.getById(expectedOkId);

        verify(orderDao, times(1)).getById(anyLong());
        assertEquals(expectedOkId, actualOrderByOkId.getId());
        assertEquals(expectedUser, actualOrderByOkId.getUser());
    }

    @Test
    void getByNonexistentOrderIdOk() {
        Long nonexistentIk = 5L;
        when(orderDao.getById(anyLong())).thenReturn(ordersStorage.stream()
                .filter(orders -> orders.getId().equals(nonexistentIk))
                .findFirst()
                .orElse(null));

        Orders actualOrderByNonexistentId = orderService.getById(nonexistentIk);

        verify(orderDao, times(1)).getById(anyLong());
        assertNull(actualOrderByNonexistentId);
    }
}