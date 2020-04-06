package com.dev.cinema.service;

import com.dev.cinema.model.Orders;
import com.dev.cinema.model.User;
import java.util.List;

public interface OrderService {

    Orders completeOrder(User user);

    List<Orders> getOrderHistory(User user);

    Orders getById(Long id);
}
