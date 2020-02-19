package com.dev.cinema.controllers;

import com.dev.cinema.model.User;
import com.dev.cinema.model.dto.request.UserRequestDto;
import com.dev.cinema.model.dto.response.UserResponseDto;
import com.dev.cinema.service.AuthenticationService;

import javax.security.sasl.AuthenticationException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public UserResponseDto login(@RequestBody UserRequestDto userRequestDto) {
        try {
            User user = authenticationService.login(userRequestDto.getEmail(),
                    userRequestDto.getPassword());
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setEmail(user.getEmail());
            return userResponseDto;
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid login or password");
        }
    }

    @PostMapping(value = "/register")
    public UserResponseDto register(@RequestBody UserRequestDto userRequestDto) {
        User user = authenticationService.register(userRequestDto.getEmail(),
                userRequestDto.getPassword());
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
    }
}