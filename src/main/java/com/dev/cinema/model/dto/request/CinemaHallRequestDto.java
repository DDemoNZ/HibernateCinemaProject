package com.dev.cinema.model.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class CinemaHallRequestDto {

    @Min(value = 25, message = "Incorrect capacity. Must be 25+.")
    private int capacity;
    @NotEmpty(message = "Description must not be empty")
    private String description;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
