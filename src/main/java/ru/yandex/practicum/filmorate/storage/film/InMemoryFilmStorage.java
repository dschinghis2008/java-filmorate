package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate LOW_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public Film createFilm(Film film) {

        try {
            if (film.getName().isBlank() || film.getName() == null) {
                throw new ValidateException("пустое наменование фильма");
            }
            if (film.getDescription().length() > 200) {
                throw new ValidateException("размер описания превышает 200 символов");
            }

            if (film.getDescription().isBlank() || film.getDescription() == null) {
                throw new ValidateException("пустое описание");
            }

            if (film.getReleaseDate().isBefore(LOW_RELEASE_DATE)) {
                throw new ValidateException("дата релиза неверна");
            }
            if (film.getDuration().isNegative() || film.getDuration().isZero()) {
                throw new ValidateException("длительность фильма должна быть положительной");
            }
        } catch (ValidateException e) {
            throw new RuntimeException(e);
        }

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Map<Integer,Film> getAll() {
        return films;
    }

    @Override
    public void deleteFilm(Long id) {
        films.remove(id);
    }

    @Override
    public void deleteAll() {
        films.clear();
    }

    @Override
    public Film getById(Long id) {
        return films.get(id);
    }

}
