package com.dev.cinema.service.impl;

import com.dev.cinema.dao.OrderDao;
import com.dev.cinema.model.Orders;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.User;
import com.dev.cinema.service.OrderService;
import com.dev.cinema.service.ShoppingCartService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ShoppingCartService shoppingCartService;

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
}
