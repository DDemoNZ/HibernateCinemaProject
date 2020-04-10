package com.dev.cinema.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.model.Role;
import com.dev.cinema.model.User;
import com.dev.cinema.model.dto.request.UserRequestDto;
import com.dev.cinema.model.dto.response.UserResponseDto;
import com.dev.cinema.service.impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserControllerTest {

    private static UserResponseDto expectedUserResponseDto;
    private static List<User> mockUserStorage;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    @BeforeAll
    static void beforeAll() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("TestEmail");
        userRequestDto.setPassword("TestPassword");

        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setRoleName("USER");

        User firstMockUser = new User();
        firstMockUser.setId(1L);
        firstMockUser.getRoles().add(userRole);
        firstMockUser.setEmail("FirstEmail");
        firstMockUser.setPassword("FirstPassword");

        User secondMockUser = new User();
        secondMockUser.setId(2L);
        secondMockUser.getRoles().add(userRole);
        secondMockUser.setEmail(userRequestDto.getEmail());
        secondMockUser.setPassword(userRequestDto.getPassword());

        User thirdMockUser = new User();
        thirdMockUser.setId(3L);
        thirdMockUser.getRoles().add(userRole);
        thirdMockUser.setEmail("ThirdEmail");
        thirdMockUser.setPassword("ThirdPassword");

        mockUserStorage = new ArrayList<>();
        mockUserStorage.add(firstMockUser);
        mockUserStorage.add(secondMockUser);
        mockUserStorage.add(thirdMockUser);

        expectedUserResponseDto = new UserResponseDto();
        expectedUserResponseDto.setEmail(userRequestDto.getEmail());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getUserByEmailOk() {
        String expectedEmailMatcher = "TestEmail";
        when(userService.findByEmail(anyString())).thenReturn(mockUserStorage.stream()
                .filter(user -> user.getEmail().equals(expectedEmailMatcher))
                .findFirst()
                .orElse(null));

        UserResponseDto actualUserResponseDto = userController.getUserByEmail(expectedEmailMatcher);

        verify(userService, times(1)).findByEmail(anyString());
        assertNotNull(actualUserResponseDto);
        assertEquals(expectedEmailMatcher, actualUserResponseDto.getEmail());
        assertEquals(expectedUserResponseDto.getEmail(), actualUserResponseDto.getEmail());
    }

    @Test
    void getUserByEmailWithNonexistentEmail() {
        String expectedEmailMatcher = "NonexistentEmail";
        when(userService.findByEmail(anyString())).thenReturn(mockUserStorage.stream()
                .filter(user -> user.getEmail().equals(expectedEmailMatcher))
                .findFirst()
                .orElse(null));

        assertThrows(RuntimeException.class,
                () -> userController.getUserByEmail(expectedEmailMatcher));
        verify(userService, times(1)).findByEmail(anyString());
    }
}