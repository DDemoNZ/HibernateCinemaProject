package com.dev.cinema.model.dto.request;

import javax.validation.constraints.NotEmpty;

public class MovieRequestDto {

    @NotEmpty(message = "Title of movie must not be empty")
    private String title;
    @NotEmpty(message = "Description of movie must not be empty")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
