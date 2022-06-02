package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Comparator<Film> comparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return o1.getCountLikes() - o2.getCountLikes();
        }
    };
    private final TreeSet<Film> rating = new TreeSet<>(comparator);


    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public void addLike(Long idFilm, Long idUser) {
        if (filmStorage.getById(idFilm) != null && userStorage.getById(idUser) != null) {
            filmStorage.getById(idFilm).addLike(idUser);
            rating.add(filmStorage.getById(idFilm));
        } else {
            throw new ValidateException("user id=" + idUser + " or film id=" + idFilm + " не найдены");
        }

    }

    public void removeLike(Long idFilm, Long idUser) {
        if (filmStorage.getById(idFilm) != null && userStorage.getById(idUser) != null) {
            filmStorage.getById(idFilm).removeLike(idUser);
            if (filmStorage.getById(idFilm).getCountLikes() == 0) {
                rating.remove(filmStorage.getById(idFilm));
            }
        } else {
            throw new ValidateException("user id=" + idUser + " or film id=" + idFilm + " не найдены");
        }

    }

    public List<Film> getMaxRating(Long countRate) {
        Long count = countRate;
        if (count == 0 || count == null) {
            count = 10L;
        }
        if (rating.size() < count) {
            count = Long.valueOf(rating.size());
        }
        List<Film> list = new ArrayList<>();
        Iterator<Film> iterator = rating.descendingIterator();
        int i = 0;
        while (iterator.hasNext() && i < count) {
            list.add(iterator.next());
            i++;
        }
        return list;
    }
}
