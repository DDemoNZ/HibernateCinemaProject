package com.dev.cinema.service.impl;

import com.dev.cinema.model.User;
import com.dev.cinema.service.AuthenticationService;
import com.dev.cinema.service.RoleService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import javax.security.sasl.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public AuthenticationServiceImpl(UserService userService,
                                     ShoppingCartService shoppingCartService,
                                     PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User login(String email, String password) throws AuthenticationException {
        User user = userService.findByEmail(email);
        if (user == null || !user.getPassword().equals(passwordEncoder.encode(password))) {
            throw new AuthenticationException("Invalid email or password");
        }
        return user;
    }

    @Override
    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.getRoles().add(roleService.getByRoleName("USER"));
        User registeredUser = userService.add(user);
        shoppingCartService.registerNewShoppingCart(registeredUser);
        return registeredUser;
    }
}
