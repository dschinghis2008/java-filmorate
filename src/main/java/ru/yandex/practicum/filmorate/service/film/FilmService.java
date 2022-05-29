package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private Comparator<Film> comparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return o1.countLikes() - o2.countLikes();
        }
    };
    private final TreeSet<Film> rating = new TreeSet<>(comparator);

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long idFilm, Long idUser) {
        filmStorage.getById(idFilm).addLike(idUser);
        rating.add(filmStorage.getById(idFilm));
    }

    public void removeLike(Long idFilm, Long idUser) {
        filmStorage.getById(idFilm).removeLike(idUser);
    }

    public List<Film> getMaxRate(int countRate) {
        int count = countRate;
        if(count == 0){
            count = 10;
        }
        if(rating.size() < count){
            count = rating.size();
        }
        List<Film> list = new ArrayList<>();
        Iterator<Film> iterator = rating.descendingIterator();
        int i = 0;
        while (iterator.hasNext() && i <= count){
            list.add(iterator.next());
            i++;
        }
        return list;
    }
}
