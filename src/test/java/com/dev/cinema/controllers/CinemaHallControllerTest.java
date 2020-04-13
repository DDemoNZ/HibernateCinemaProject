package com.dev.cinema.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.dto.request.CinemaHallRequestDto;
import com.dev.cinema.model.dto.response.CinemaHallResponseDto;
import com.dev.cinema.service.impl.CinemaHallServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CinemaHallControllerTest {

    private static CinemaHallRequestDto cinemaHallRequestDto;
    private static CinemaHallResponseDto expectedCinemaHallResponseDto;
    private static CinemaHall expectedCinemaHall;
    private static List<CinemaHall> mockCinemaHallStorage;
    private static CinemaHall expectedMockFirstCinemaHall;
    private static CinemaHall expectedMockSecondCinemaHall;
    private static CinemaHall expectedMockThirdCinemaHall;

    @InjectMocks
    private CinemaHallController cinemaHallController;

    @Mock
    private CinemaHallServiceImpl cinemaHallService;

    @BeforeAll
    static void beforeAll() {
        cinemaHallRequestDto = new CinemaHallRequestDto();
        cinemaHallRequestDto.setCapacity(20);
        cinemaHallRequestDto.setDescription("TestDescription");

        expectedCinemaHall = new CinemaHall();
        expectedCinemaHall.setId(1L);
        expectedCinemaHall.setCapacity(cinemaHallRequestDto.getCapacity());
        expectedCinemaHall.setDescription(cinemaHallRequestDto.getDescription());

        expectedCinemaHallResponseDto = new CinemaHallResponseDto();
        expectedCinemaHallResponseDto.setCapacity(cinemaHallRequestDto.getCapacity());
        expectedCinemaHallResponseDto.setDescription(cinemaHallRequestDto.getDescription());

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
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addCinemaHallOk() {
        when(cinemaHallService.add(any())).thenReturn(expectedCinemaHall);

        CinemaHallResponseDto actualCinemaHallResponseDto =
                cinemaHallController.addCinemaHall(cinemaHallRequestDto);

        verify(cinemaHallService, times(1)).add(any());
        assertNotNull(actualCinemaHallResponseDto);
        assertEquals(expectedCinemaHallResponseDto.getCapacity(),
                actualCinemaHallResponseDto.getCapacity());
        assertEquals(expectedCinemaHallResponseDto.getDescription(),
                actualCinemaHallResponseDto.getDescription());
    }

    @Test
    void getAllCinemaHallsOk() {
        when(cinemaHallService.getAll()).thenReturn(mockCinemaHallStorage);

        List<CinemaHallResponseDto> actualAllCinemaHallResponse =
                cinemaHallController.getAllCinemaHalls();

        verify(cinemaHallService, times(1)).getAll();
        assertNotNull(actualAllCinemaHallResponse);
        assertEquals(expectedMockFirstCinemaHall.getCapacity(),
                actualAllCinemaHallResponse.get(0).getCapacity());
        assertEquals(expectedMockFirstCinemaHall.getDescription(),
                actualAllCinemaHallResponse.get(0).getDescription());
        assertEquals(expectedMockSecondCinemaHall.getCapacity(),
                actualAllCinemaHallResponse.get(1).getCapacity());
        assertEquals(expectedMockSecondCinemaHall.getDescription(),
                actualAllCinemaHallResponse.get(1).getDescription());
    }

    @Test
    void getAllCinemaHallsNonexistent() {
        when(cinemaHallService.getAll()).thenReturn(new ArrayList<CinemaHall>());

        List<CinemaHallResponseDto> actualCinemaHallStorageResponse =
                cinemaHallController.getAllCinemaHalls();

        verify(cinemaHallService, times(1)).getAll();
        assertNotNull(actualCinemaHallStorageResponse);
        assertEquals(0, actualCinemaHallStorageResponse.size());
    }
}