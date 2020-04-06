package com.dev.cinema.service;

import com.dev.cinema.model.User;
import javax.security.sasl.AuthenticationException;

public interface AuthenticationService {

    User login(String email, String password) throws AuthenticationException;

    User register(String email, String password);
}
