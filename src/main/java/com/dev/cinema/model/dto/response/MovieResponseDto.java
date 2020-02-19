package com.dev.cinema.model.dto.response;

public class MovieResponseDto {

    private String title;
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

    @Override
    public String toString() {
        return "MovieDto{"
                + " title='" + title + '\''
                + ", description='" + description + '\''
                + '}';
    }
}
