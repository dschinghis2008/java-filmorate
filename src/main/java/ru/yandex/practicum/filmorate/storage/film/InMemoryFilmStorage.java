package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate LOW_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private Long newId = 1L;

    private Long getNewId() {
        return newId++;
    }

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
            if (film.getDuration() <= 0) {
                throw new ValidateException("длительность фильма должна быть положительной");
            }
        } catch (ValidateException e) {
            throw new RuntimeException(e);
        }
        film.setId(getNewId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() <= 0) {
            throw new ValidateException("id должен быть > 0");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> list = new ArrayList<>();
        for (Long id : films.keySet()) {
            list.add(films.get(id));
        }
        return list;
    }

    @Override
    public Film deleteFilm(Long id) {
        Film film = films.get(id);
        films.remove(id);
        return film;
    }

    @Override
    public void deleteAll() {
        films.clear();
    }

    @Override
    public Film getById(Long id) {
        if (films.get(id) == null) {
            throw new ValidateException("film not found");
        }
        return films.get(id);
    }

}
