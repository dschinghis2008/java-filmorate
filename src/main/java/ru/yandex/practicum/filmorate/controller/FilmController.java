package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
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
    public Map<Integer,Film> getFilms() {
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
    public void deleteFilm(@PathVariable long id){
        inMemoryFilmStorage.deleteFilm(id);
        log.debug("удален фильм {}", id);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id){
        log.debug("запрошен фильм {}", id);
        return inMemoryFilmStorage.getById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id,@PathVariable long userId){
        filmService.addLike(id,userId);
        log.debug("лайкнут фильм {}", id," юсером {}",userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id,@PathVariable long userId){
        filmService.removeLike(id,userId);
        log.debug("отозван лайк фильма {}", id," юсером {}",userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam int count){
        log.debug("запрошены популярные фильмы в количестве {}", (count == 0 ? 10 : count));
        return filmService.getMaxRate(count);
    }

}
