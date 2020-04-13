package com.dev.cinema.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.model.User;
import com.dev.cinema.model.dto.request.UserAuthenticateRequestDto;
import com.dev.cinema.model.dto.response.UserResponseDto;
import com.dev.cinema.service.AuthenticationService;
import java.util.ArrayList;
import java.util.List;
import javax.security.sasl.AuthenticationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AuthenticationControllerTest {

    private static User expectedUser;
    private static UserAuthenticateRequestDto userTestRequestDto;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeAll
    static void beforeAll() {
        userTestRequestDto = new UserAuthenticateRequestDto();
        userTestRequestDto.setEmail("Test");
        userTestRequestDto.setPassword("Test");
        userTestRequestDto.setRepeatedPassword("Test");

        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail("Test");
        expectedUser.setPassword("Test");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loginOk() throws AuthenticationException {
        String expectedLoginResponse = "Success login";

        UserAuthenticateRequestDto userAuthenticationRequestDto = new UserAuthenticateRequestDto();
        userAuthenticationRequestDto.setEmail(expectedUser.getEmail());
        userAuthenticationRequestDto.setPassword(expectedUser.getPassword());
        userAuthenticationRequestDto.setRepeatedPassword(expectedUser.getPassword());

        when(authenticationService.login(anyString(), anyString())).thenReturn(expectedUser);

        String actualLoginResponse = authenticationController.login(userAuthenticationRequestDto);

        verify(authenticationService, times(1)).login(anyString(), anyString());
        assertEquals(expectedLoginResponse, actualLoginResponse);
    }

    @Test
    void loginWithNonexistentUser() throws AuthenticationException {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail("Nonexistent");
        expectedUser.setPassword("Nonexistent");

        String expectedLoginResponse = "Invalid login or password";

        when(authenticationService.login(anyString(), anyString()))
                .thenThrow(new AuthenticationException("Invalid login or password"));

        String actualLoginResponseWithNonexistentUser =
                authenticationController.login(userTestRequestDto);

        verify(authenticationService, times(1)).login(anyString(), anyString());
        assertEquals(expectedLoginResponse, actualLoginResponseWithNonexistentUser);
    }

    @Test
    void registerOk() {
        when(authenticationService.register(anyString(), anyString())).thenReturn(expectedUser);

        UserResponseDto actualRegisteredUserResponseDto =
                authenticationController.register(userTestRequestDto);

        verify(authenticationService, times(1)).register(anyString(), anyString());
        assertEquals(expectedUser.getEmail(), actualRegisteredUserResponseDto.getEmail());
    }
}