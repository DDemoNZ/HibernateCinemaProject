package com.dev.cinema.dao;

import com.dev.cinema.model.Orders;
import com.dev.cinema.model.User;
import java.util.List;

public interface OrderDao {

    Orders add(Orders order);

    List<Orders> getUserOrderHistory(User user);

    Orders getById(Long id);
}
