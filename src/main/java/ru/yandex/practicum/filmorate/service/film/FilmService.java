package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("dbFilmStorage") FilmStorage filmStorage, @Qualifier("dbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public void addLike(Long idFilm, Long idUser) {
        if (filmStorage.getById(idFilm) != null && userStorage.getById(idUser) != null
        && idFilm > 0 && idUser > 0) {
            filmStorage.addLike(idUser,idFilm);
        } else {
            throw new NotFoundException("user id=" + idUser + " or film id=" + idFilm + " не найдены");
        }

    }

    public void removeLike(Long idFilm, Long idUser) {
        if (filmStorage.getById(idFilm) != null && userStorage.getById(idUser) != null) {
            filmStorage.removeLike(idUser,idFilm);
        } else {
            throw new ValidateException("user id=" + idUser + " or film id=" + idFilm + " не найдены");
        }

    }

    public List<Optional<Film>> getMaxRating(Long countRate) {
        Long count = countRate;
        if (count == 0 || count == null) {
            count = 10L;
        }

        List<Optional<Film>> listOrder = filmStorage.getOrderRate();
        if(listOrder.size() < count){
            count = Long.valueOf(listOrder.size());
        }
        List<Optional<Film>> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(listOrder.get(i));
        }

        return list;
    }
}
