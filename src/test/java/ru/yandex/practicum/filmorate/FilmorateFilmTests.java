package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmorateFilmTests {

    @Autowired
    private FilmController filmController;


    @Test
    public void filmControllerValidEntityTest() throws ValidateException {
        Film film = new Film(1L, "Example", "desc Example", LocalDate.of(2000, 1, 1)
                , 100, 0L);
        filmController.createFilm(film);

        Assertions.assertEquals(filmController.getCountFilms(), 1, "ожидается - добавлен 1 фильм");
    }

    @Test
    public void filmControllerInvalidNameTest() {
        Film film = new Film(1L, "", "desc Example", LocalDate.of(2000, 1, 1)
                , 100, 0L);

        assertThrows(ValidateException.class, () -> filmController.createFilm(film));
        film.setName(null);
        assertThrows(NullPointerException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerInvalidLengthDescriptionTest() {
        String s = "";
        for (int i = 0; i < 41; i++) {
            s += "desc ";
        }
        Film film = new Film(1L, "Example", s, LocalDate.of(2000, 1, 1)
                , 100, 0L);

        assertThrows(ValidateException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerInvalidDateReliaseTest() {
        Film film = new Film(1L, "Example", "desc Example", LocalDate.of(1895, 12, 27)
                , 100, 0L);
        assertThrows(ValidateException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerInvalidDurationTest() {
        Film film = new Film(1L, "Example", "desc Example", LocalDate.of(2000, 1, 1)
                , 100, 0L);

        assertThrows(ValidateException.class, () -> filmController.createFilm(film));
        film.setDuration(0);
        assertThrows(ValidateException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerUpdateEntityTest() throws ValidateException {
        Film film = new Film(1L, "Example", "desc Example", LocalDate.of(2000, 1, 1)
                , 100, 0L);


        Film filmUpd = new Film(1L, "Example2", "desc Example2", LocalDate.of(2002, 1, 1)
                , 100, 0L);

        filmController.deleteFilms();
        filmController.createFilm(film);

        filmController.updateFilm(filmUpd);
        Assertions.assertEquals(filmController.getCountFilms(), 1, "ожидается - обновлен 1 фильм");
    }

    @Test
    public void filmControllerGetFilmsTest() throws ValidateException {
        Film film = new Film(1L, "Example", "desc Example", LocalDate.of(2000, 1, 1)
                , 100, 0L);

        Film film2 = new Film(2L, "Example", "desc Example", LocalDate.of(2000, 1, 1)
                , 100, 0L);

        filmController.createFilm(film);
        filmController.createFilm(film2);
        List<Film> films = filmController.getFilms();

        Assertions.assertEquals(films.size(), 2, "ожидается - получено 2 фильма");
    }
}
