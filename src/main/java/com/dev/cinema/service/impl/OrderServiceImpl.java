package com.dev.cinema.service.impl;

import com.dev.cinema.dao.OrderDao;
import com.dev.cinema.lib.Inject;
import com.dev.cinema.lib.Service;
import com.dev.cinema.model.Orders;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.User;
import com.dev.cinema.service.OrderService;
import com.dev.cinema.service.ShoppingCartService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Inject
    private OrderDao orderDao;

    @Inject
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
