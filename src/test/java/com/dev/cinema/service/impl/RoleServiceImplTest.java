package com.dev.cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.dao.RoleDao;
import com.dev.cinema.model.Role;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RoleServiceImplTest {

    private static Role expectedRole;
    private static List<Role> mockRoleStorage;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleDao roleDao;

    @BeforeAll
    static void beforeAll() {
        expectedRole = new Role();
        expectedRole.setId(1L);
        expectedRole.setRoleName("TEST");

        Role secondTestMockRole = new Role();
        secondTestMockRole.setId(2L);
        secondTestMockRole.setRoleName("SECOND");

        Role thirdTestMockRole = new Role();
        thirdTestMockRole.setId(3L);
        thirdTestMockRole.setRoleName("THIRD");

        mockRoleStorage = new ArrayList<>();
        mockRoleStorage.add(expectedRole);
        mockRoleStorage.add(secondTestMockRole);
        mockRoleStorage.add(thirdTestMockRole);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void addRoleOk() {
        when(roleDao.add(any())).thenReturn(expectedRole);

        Role testRole = new Role();
        testRole.setRoleName("TEST");

        Role actualRole = roleService.add(testRole);

        verify(roleDao, times(1)).add(any());

        assertEquals(expectedRole.getId(), actualRole.getId());
        assertEquals(expectedRole.getRoleName(), actualRole.getRoleName());
    }

    @Test
    void getByRoleNameOk() {
        String expectedRoleName = "TEST";
        when(roleDao.getByRoleName(anyString())).thenReturn(mockRoleStorage.stream()
                .filter(role -> role.getRoleName().equals(expectedRoleName))
                .findFirst()
                .orElse(null));

        Role actualRoleByRoleName = roleService.getByRoleName(expectedRoleName);

        verify(roleDao, times(1)).getByRoleName(anyString());

        assertNotNull(actualRoleByRoleName.getId());
        assertEquals(expectedRoleName, actualRoleByRoleName.getRoleName());
    }

    @Test
    void getByNonexistentRoleName() {
        String expectedRoleName = "NonexistentRole";
        when(roleDao.getByRoleName(anyString())).thenReturn(mockRoleStorage.stream()
                .filter(role -> role.getRoleName().equals(expectedRoleName))
                .findFirst()
                .orElse(null));

        Role actualRoleByNonexistentRole = roleService.getByRoleName(expectedRoleName);

        verify(roleDao, times(1)).getByRoleName(anyString());

        assertNull(actualRoleByNonexistentRole);
    }
}