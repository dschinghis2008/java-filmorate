package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public void createFilm(@RequestBody Film film) throws ValidateException {

        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidateException("пустое наменование фильма");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidateException("размер описания превышает 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("дата релиза неверна");
        }
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new ValidateException("длительность фильма должна быть положительной");
        }
        films.put(film.getId(), film);
        log.debug("добавлен фильм: " + film.toString());
    }

    @PutMapping
    public void updateFilm(@RequestBody Film film) throws ValidateException {
        if (!films.containsKey(film.getId())) {
            throw new ValidateException("не найден фильм для обновления его данных");
        }
        films.put(film.getId(), film);
        log.debug("обновлен фильм: " + film.toString());
    }

    @GetMapping
    public String getFilms() {
        String result = "";
        for (Integer id : films.keySet()) {
            result += films.get(id).getName() + " " + films.get(id).getReleaseDate() + " " + films.get(id).getDuration()
                    + "\n";
        }
        log.debug("запрошены фильмы: " + result);
        return result;
    }

    public int getCountFilms() {
        return films.size();
    }

}
