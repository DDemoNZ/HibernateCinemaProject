package com.dev.cinema.model.dto.response;

public class CinemaHallResponseDto {

    private int capacity;
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

    @Override
    public String toString() {
        return "CinemaHallDto{"
                + " capacity=" + capacity
                + ", description='" + description + '\''
                + '}';
    }
}
