package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Long rate;
    private final Mpa mpa;
    private List<Genre> genres;

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration, Long rate, Mpa mpa,List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }


}
