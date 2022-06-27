package ru.yandex.practicum.filmorate.model;

public class Genre {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
