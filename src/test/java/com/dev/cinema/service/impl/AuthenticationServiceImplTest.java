package com.dev.cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.model.Role;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.User;
import javax.security.sasl.AuthenticationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private static Role mockUserRole;
    private static User expectedUser;


    @BeforeAll
    static void beforeAll() {
        mockUserRole = new Role();
        mockUserRole.setId(1L);
        mockUserRole.setRoleName("USER");

        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setPassword("Secret : TestUser");
        expectedUser.setEmail("TestUser");
        expectedUser.getRoles().add(mockUserRole);

    }

    @Test
    void loginOk() throws AuthenticationException {
        String expectedPassword = "TestUser";
        String expectedEmail = "TestUser";

        Role mockUserRole = new Role();
        mockUserRole.setId(1L);
        mockUserRole.setRoleName("USER");

        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("Secret : TestUser");
        testUser.setEmail("TestUser");
        testUser.getRoles().add(mockUserRole);

        when(userService.findByEmail(expectedEmail)).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("Secret : " + expectedPassword);

        User actualUserFromLogin = authenticationService.login(expectedEmail, expectedPassword);

        verify(userService, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(1)).encode(any());
        assertEquals(expectedUser, actualUserFromLogin);
    }

    @Test
    void loginWithNonValid() throws AuthenticationException {
        String expectedPassword = "Secret : TestUser";
        String expectedEmail = "Test";

        when(userService.findByEmail(expectedEmail)).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn(expectedPassword);

        assertThrows(AuthenticationException.class, () -> authenticationService.login(expectedEmail,
            expectedPassword));
        verify(userService, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(0)).encode(any());
    }

    @Test
    void registerOk() {
        User testUser = new User();
        testUser.setEmail("Email");
        testUser.setPassword("Psw");
        testUser.getRoles().add(mockUserRole);

        User expectedTestUser = new User();
        expectedTestUser.setId(1L);
        expectedTestUser.setPassword(testUser.getPassword());
        expectedTestUser.setEmail(testUser.getEmail());
        expectedTestUser.setRoles(testUser.getRoles());

       ShoppingCart expectedShoppingCart = new ShoppingCart();
       expectedShoppingCart.setUser(expectedTestUser);
       expectedShoppingCart.setId(1L);

       when(passwordEncoder.encode(anyString())).thenReturn("Secret : " + testUser.getPassword());
       when(roleService.getByRoleName("USER")).thenReturn(mockUserRole);
       when(userService.add(any())).thenReturn(expectedTestUser);
       when(shoppingCartService.registerNewShoppingCart(testUser)).thenReturn(expectedShoppingCart);

        User actualRegisteredUser = authenticationService.register(testUser.getEmail(), testUser.getPassword());

        verify(passwordEncoder, times(1)).encode(any());
        verify(roleService, times(1)).getByRoleName(anyString());
        verify(userService, times(1)).add(any());
        verify(shoppingCartService, times(1)).registerNewShoppingCart(any());
        assertEquals(expectedTestUser, actualRegisteredUser);
    }
}