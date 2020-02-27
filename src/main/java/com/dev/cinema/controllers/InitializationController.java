package com.dev.cinema.controllers;

import com.dev.cinema.model.Role;
import com.dev.cinema.model.User;
import com.dev.cinema.service.RoleService;
import com.dev.cinema.service.UserService;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitializationController {

    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    public InitializationController(RoleService roleService, UserService userService,
                                    PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        roleService.add(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("USER");
        roleService.add(userRole);

        User user = new User();
        user.setEmail("a");
        user.setPassword(passwordEncoder.encode("a"));
        user.getRoles().add(roleService.getByRoleName("USER"));
        userService.add(user);

        User admin = new User();
        admin.setEmail("b");
        admin.setPassword(passwordEncoder.encode("b"));
        admin.getRoles().add(roleService.getByRoleName("ADMIN"));
        userService.add(admin);

    }
}
