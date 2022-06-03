package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    public FilmController(FilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidateException {
        inMemoryFilmStorage.createFilm(film);
        log.debug("добавлен фильм: {}", film.toString());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidateException {
        log.debug("обновлен фильм: {}", film.toString());
        return inMemoryFilmStorage.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.debug("запрошены все фильмы");
        return inMemoryFilmStorage.getAll();
    }

    public int getCountFilms() {
        return inMemoryFilmStorage.getAll().size();
    }

    @DeleteMapping
    public void deleteFilms() {
        inMemoryFilmStorage.deleteAll();
        log.debug("удалены все фильмы");
    }

    @DeleteMapping("/{id}")
    public Film deleteFilm(@PathVariable Long id) {
        Film film = inMemoryFilmStorage.deleteFilm(id);
        log.debug("удален фильм {}", id);
        return film;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.debug("запрошен фильм {}", id);
        return inMemoryFilmStorage.getById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
        log.debug("лайкнут фильм {}", id, " , пользователем {}", userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        log.debug("отозван лайк фильма {}", id, " , пользователем {}", userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "0") Long count) {
        log.debug("запрошены популярные фильмы в количестве {}", ((count == 0) || (count == null) ? 10 : count));
        return filmService.getMaxRating(count);
    }

}
