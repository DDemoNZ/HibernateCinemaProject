package com.dev.cinema.service.impl;

import com.dev.cinema.dao.UserDao;
import com.dev.cinema.model.User;
import com.dev.cinema.service.AuthenticationService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import com.dev.cinema.util.HashUtil;

import javax.security.sasl.AuthenticationException;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDao userDao;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;

    public AuthenticationServiceImpl(UserDao userDao, UserService userService,
                                     ShoppingCartService shoppingCartService) {
        this.userDao = userDao;
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public User login(String email, String password) throws AuthenticationException {
        User user = userDao.findByEmail(email);
        if (user == null || !user.getPassword()
                .equals(HashUtil.hashPassword(password, user.getSalt()))) {
            throw new AuthenticationException("Invalid email or password");
        }
        return user;
    }

    @Override
    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        User registeredUser = userService.add(user);
        shoppingCartService.registerNewShoppingCart(registeredUser);
        return registeredUser;
    }
}
