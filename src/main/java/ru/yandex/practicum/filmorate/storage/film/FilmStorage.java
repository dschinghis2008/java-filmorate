package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Film createFilm(Film film);
    Film updateFilm(Film film);
    Map<Integer,Film> getAll();
    void deleteFilm(Long id);
    void deleteAll();
    Film getById(Long id);
}
