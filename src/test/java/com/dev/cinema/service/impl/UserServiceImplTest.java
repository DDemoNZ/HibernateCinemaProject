package com.dev.cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.dao.UserDao;
import com.dev.cinema.model.Role;
import com.dev.cinema.model.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceImplTest {

    private static User testUser;
    private static User expectedMockUser;
    private static List<User> userStorage;
    private static User firstUserInStorage;
    private static User secondUserInStorage;
    private static User thirdUserInStorage;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        testUser = new User();
        testUser.setEmail("TestEmail");
        testUser.setPassword("TestPsw");
        Role userRole = new Role();
        userRole.setRoleName("USER");
        userRole.setId(1L);
        testUser.getRoles().add(userRole);

        expectedMockUser = new User();
        expectedMockUser.setId(1L);
        expectedMockUser.setEmail(testUser.getEmail());
        expectedMockUser.setPassword(testUser.getPassword());
        expectedMockUser.getRoles().addAll(testUser.getRoles());

        userStorage = new ArrayList<>();
        firstUserInStorage = new User();
        firstUserInStorage.setId(1L);
        firstUserInStorage.setEmail("FirstEmail");
        firstUserInStorage.setPassword("FirstPassword");
        firstUserInStorage.getRoles().add(userRole);

        secondUserInStorage = new User();
        secondUserInStorage.setId(2L);
        secondUserInStorage.setEmail("SecondEmail");
        secondUserInStorage.setPassword("SecondPassword");
        secondUserInStorage.getRoles().add(userRole);

        thirdUserInStorage = new User();
        thirdUserInStorage.setId(3L);
        thirdUserInStorage.setEmail("ThirdEmail");
        thirdUserInStorage.setPassword("ThirdPassword");
        thirdUserInStorage.getRoles().add(userRole);

        userStorage.add(firstUserInStorage);
        userStorage.add(secondUserInStorage);
        userStorage.add(thirdUserInStorage);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void add() {
        when(userDao.add(testUser)).thenReturn(expectedMockUser);

        User actualUser = userService.add(testUser);

        verify(userDao, times(1)).add(any());

        assertNotNull(actualUser);
        assertNotNull(actualUser.getId());
        assertEquals(expectedMockUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedMockUser.getPassword(), actualUser.getPassword());
    }

    @Test
    void findByEmail() {
        String expectedEmailMatcher = "SecondEmail";
        when(userDao.findByEmail(anyString())).thenReturn(userStorage.stream()
                .filter(user -> user.getEmail().equals(expectedEmailMatcher))
                .findFirst()
                .orElse(null));

        User actualUser = userService.findByEmail(expectedEmailMatcher);

        verify(userDao, times(1)).findByEmail(anyString());

        assertNotNull(actualUser);
        assertNotNull(actualUser.getId());
        assertEquals(expectedEmailMatcher, actualUser.getEmail());
    }

    @Test
    void findByEmailWithNonexistentEmail() {
        String expectedEmailMatcher = "NonexistentEmail";
        when(userDao.findByEmail(anyString())).thenReturn(userStorage.stream()
                .filter(user -> user.getEmail().equals(expectedEmailMatcher))
                .findFirst()
                .orElse(null));

        User actualUser = userService.findByEmail(expectedEmailMatcher);

        verify(userDao, times(1)).findByEmail(anyString());

        assertNull(actualUser);
    }

    @Test
    void getByIdOk() {
        Long expectedIdMatcher = 2L;
        when(userDao.getById(anyLong())).thenReturn(userStorage.stream()
                .filter(user -> user.getId().equals(expectedIdMatcher))
                .findFirst()
                .orElse(null));

        User actualUser = userService.getById(expectedIdMatcher);

        verify(userDao, times(1)).getById(anyLong());

        assertNotNull(actualUser);
        assertNotNull(actualUser.getId());
        assertEquals(secondUserInStorage.getEmail(), actualUser.getEmail());
        assertEquals(secondUserInStorage.getPassword(), actualUser.getPassword());
    }

    @Test
    void getByIdWithNonexistentId() {
        Long expectedIdMatcher = 5L;
        when(userDao.getById(anyLong())).thenReturn(userStorage.stream()
                .filter(user -> user.getId().equals(expectedIdMatcher))
                .findFirst()
                .orElse(null));

        User actualUser = userService.getById(expectedIdMatcher);

        verify(userDao, times(1)).getById(anyLong());

        assertNull(actualUser);
    }
}