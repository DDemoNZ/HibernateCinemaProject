package com.dev.cinema.service;

import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.User;

public interface ShoppingCartService {

    ShoppingCart addSession(MovieSession movieSession, User user);

    ShoppingCart getByUser(User user);

    ShoppingCart registerNewShoppingCart(User user);

    ShoppingCart clear(ShoppingCart shoppingCart);

    ShoppingCart getById(Long id);
}
