package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate LOW_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private Long newId = 1L;

    private Long getNewId() {
        return newId++;
    }

    @Override
    public Optional<Film> createFilm(Film film) {
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
        return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (film.getId() <= 0) {
            throw new ValidateException("id должен быть > 0");
        }
        films.put(film.getId(), film);
        return Optional.of(film);
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
    public void deleteFilm(Long id) {
        Film film = films.get(id);
        films.remove(id);
    }

    @Override
    public void deleteAll() {
        films.clear();
    }

    @Override
    public Optional<Film> getById(Long id) {
        if (films.get(id) == null) {
            throw new ValidateException("film not found");
        }
        return Optional.of(films.get(id));
    }

    @Override
    public void addLike(Long idUser, Long idFilm) {

    }

    @Override
    public void removeLike(Long idUser, Long idFilm) {

    }

    @Override
    public List<Optional<Film>> getOrderRate(Integer limit) {
        return null;
    }
}
