package com.dev.cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.dao.CinemaHallDao;
import com.dev.cinema.model.CinemaHall;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CinemaHallServiceImplTest {

    private static List<CinemaHall> mockCinemaHallStorage;
    private static CinemaHall expectedMockFirstCinemaHall;
    private static CinemaHall expectedMockSecondCinemaHall;
    private static CinemaHall expectedMockThirdCinemaHall;
    private static CinemaHall testCinemaHall;
    private static CinemaHall expectedCinemaHall;

    @InjectMocks
    private CinemaHallServiceImpl cinemaHallService;

    @Mock
    private CinemaHallDao cinemaHallDao;

    @BeforeAll
    public static void beforeAll() {
        mockCinemaHallStorage = new ArrayList<>();

        expectedMockFirstCinemaHall = new CinemaHall();
        expectedMockFirstCinemaHall.setId(1L);
        expectedMockFirstCinemaHall.setCapacity(10);
        expectedMockFirstCinemaHall.setDescription("FirstCinemaHall");

        expectedMockSecondCinemaHall = new CinemaHall();
        expectedMockSecondCinemaHall.setId(2L);
        expectedMockSecondCinemaHall.setCapacity(20);
        expectedMockSecondCinemaHall.setDescription("SecondCinemaHall");

        expectedMockThirdCinemaHall = new CinemaHall();
        expectedMockThirdCinemaHall.setId(3L);
        expectedMockThirdCinemaHall.setCapacity(30);
        expectedMockThirdCinemaHall.setDescription("ThirdCinemaHall");

        mockCinemaHallStorage.add(expectedMockFirstCinemaHall);
        mockCinemaHallStorage.add(expectedMockSecondCinemaHall);
        mockCinemaHallStorage.add(expectedMockThirdCinemaHall);

        testCinemaHall = new CinemaHall();
        testCinemaHall.setCapacity(30);
        testCinemaHall.setDescription("TestDescription");

        expectedCinemaHall = new CinemaHall();
        expectedCinemaHall.setId(1L);
        expectedCinemaHall.setCapacity(testCinemaHall.getCapacity());
        expectedCinemaHall.setDescription(testCinemaHall.getDescription());
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCinemaHalByIdOk() {
        Long expectedCinemaHallId = 2L;
        when(cinemaHallDao.getById(anyLong())).thenReturn(mockCinemaHallStorage.stream()
                .filter(cinemaHall -> cinemaHall.getId().equals(expectedCinemaHallId))
                .findFirst()
                .orElse(null));

        CinemaHall actualCinemaHallById = cinemaHallService.getById(expectedCinemaHallId);

        verify(cinemaHallDao, times(1)).getById(anyLong());
        assertNotNull(actualCinemaHallById);
        assertEquals(expectedMockSecondCinemaHall.getDescription(),
                actualCinemaHallById.getDescription());
        assertEquals(expectedMockSecondCinemaHall.getCapacity(),
                actualCinemaHallById.getCapacity());
    }

    @Test
    public void getCinemaHallByNonexistentId() {
        Long expectedCinemaHallId = 5L;
        when(cinemaHallDao.getById(anyLong())).thenReturn(mockCinemaHallStorage.stream()
                .filter(cinemaHall -> cinemaHall.getId().equals(expectedCinemaHallId))
                .findFirst()
                .orElse(null));

        CinemaHall actualCinemaHallByNonexistentId = cinemaHallService.getById(expectedCinemaHallId);

        verify(cinemaHallDao, times(1)).getById(anyLong());
        assertNull(actualCinemaHallByNonexistentId);
    }

    @Test
    public void addCinemaHallOk() {
        when(cinemaHallDao.add(testCinemaHall)).thenReturn(expectedCinemaHall);

        CinemaHall actualCinemaHall = cinemaHallService.add(testCinemaHall);

        verify(cinemaHallDao, times(1)).add(any());
        assertNotNull(actualCinemaHall);
        assertEquals(Long.valueOf(1L), actualCinemaHall.getId());
        assertEquals(expectedCinemaHall.getCapacity(), actualCinemaHall.getCapacity());
        assertEquals(expectedCinemaHall.getDescription(), actualCinemaHall.getDescription());
    }

    @Test
    public void getAllCinemaHallOk() {
        when(cinemaHallDao.getAll()).thenReturn(mockCinemaHallStorage);

        List<CinemaHall> actualCinemaHallStorage = cinemaHallService.getAll();

        verify(cinemaHallDao, times(1)).getAll();
        assertNotNull(actualCinemaHallStorage);
        assertEquals(expectedMockFirstCinemaHall, actualCinemaHallStorage.get(0));
        assertEquals(expectedMockSecondCinemaHall, actualCinemaHallStorage.get(1));
    }

    @Test
    public void getAllCinemaHallWithNonexistent() {
        when(cinemaHallDao.getAll()).thenReturn(new ArrayList<CinemaHall>());

        List<CinemaHall> actualCinemaHallStorage = cinemaHallService.getAll();

        verify(cinemaHallDao, times(1)).getAll();
        assertNotNull(actualCinemaHallStorage);
        assertEquals(0, actualCinemaHallStorage.size());
        assertThrows(Exception.class, () -> actualCinemaHallStorage.get(0));
        assertThrows(Exception.class, () -> actualCinemaHallStorage.get(1));
    }
}
