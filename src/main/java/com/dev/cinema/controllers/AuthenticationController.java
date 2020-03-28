package com.dev.cinema.controllers;

import com.dev.cinema.model.User;
import com.dev.cinema.model.dto.request.UserAuthenticateRequestDto;
import com.dev.cinema.model.dto.response.UserResponseDto;
import com.dev.cinema.service.AuthenticationService;

import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthenticationController {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid UserAuthenticateRequestDto
                                userAuthenticateRequestDto) {
        try {
            authenticationService.login(userAuthenticateRequestDto.getEmail(),
                    userAuthenticateRequestDto.getPassword());
            LOGGER.debug("Login success");
            return "Success login";
        } catch (AuthenticationException e) {
            LOGGER.error("Invalid login or password. Access denied.", e);
            return "Invalid login or password";
        }
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserAuthenticateRequestDto
                                            userAuthenticateRequestDto) {
        User user = authenticationService.register(userAuthenticateRequestDto.getEmail(),
                userAuthenticateRequestDto.getPassword());
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
    }
}
