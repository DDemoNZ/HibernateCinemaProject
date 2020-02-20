package com.dev.cinema.service.impl;

import com.dev.cinema.dao.OrderDao;
import com.dev.cinema.model.Orders;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.User;
import com.dev.cinema.service.OrderService;
import com.dev.cinema.service.ShoppingCartService;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ShoppingCartService shoppingCartService;

    public OrderServiceImpl(OrderDao orderDao, ShoppingCartService shoppingCartService) {
        this.orderDao = orderDao;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public Orders completeOrder(User user) {
        ShoppingCart shoppingCartByUser = shoppingCartService.getByUser(user);
        Orders order = new Orders();
        order.setTickets(shoppingCartByUser.getTickets());
        order.setUser(user);

        shoppingCartService.clear(shoppingCartByUser);
        return orderDao.add(order);
    }

    @Override
    public List<Orders> getOrderHistory(User user) {
        return orderDao.getUserOrderHistory(user);
    }

    @Override
    public Orders getById(Long id) {
        return orderDao.getById(id);
    }
}
