package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmorateFilmTests {

    @Autowired
    FilmController filmController;


    @Test
    public void filmControllerValidEntityTest() throws ValidateException {
        Film film = new Film();
        film.setId(1);
        film.setName("Example");
        film.setDescription("desc Example");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(Duration.ofHours(1));
        filmController.createFilm(film);

        Assertions.assertEquals(filmController.getCountFilms(), 1, "ожидается - добавлен 1 фильм");
    }

    @Test
    public void filmControllerInvalidNameTest() {
        Film film = new Film();
        film.setId(1);
        film.setName("");
        film.setDescription("desc Example");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(Duration.ofHours(1));

        assertThrows(ValidateException.class, () -> filmController.createFilm(film));
        film.setName(null);
        assertThrows(NullPointerException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerInvalidLengthDescriptionTest() {
        Film film = new Film();
        film.setId(1);
        film.setName("film1");
        String s = "";
        for (int i = 0; i < 41; i++) {
            s += "desc ";
        }
        film.setDescription(s);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(Duration.ofHours(1));

        assertThrows(ValidateException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerInvalidDateReliaseTest() {
        Film film = new Film();
        film.setId(1);
        film.setName("film1");
        film.setDescription("film1");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(Duration.ofHours(1));

        assertThrows(ValidateException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerInvalidDurationTest() {
        Film film = new Film();
        film.setId(1);
        film.setName("film1");
        film.setDescription("film1");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(Duration.ofHours(-1));

        assertThrows(ValidateException.class, () -> filmController.createFilm(film));
        film.setDuration(Duration.ofHours(0));
        assertThrows(ValidateException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void filmControllerUpdateEntityTest() throws ValidateException {
        Film film = new Film();
        film.setId(1);
        film.setName("Example");
        film.setDescription("desc Example");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(Duration.ofHours(1));

        Film filmUpd = new Film();
        filmUpd.setId(1);
        filmUpd.setName("Example2");
        filmUpd.setDescription("desc Example2");
        filmUpd.setReleaseDate(LocalDate.of(2002, 1, 1));
        filmUpd.setDuration(Duration.ofHours(1));

        filmController.clearFilms();
        filmController.createFilm(film);

        filmController.updateFilm(filmUpd);
        Assertions.assertEquals(filmController.getCountFilms(), 1, "ожидается - обновлен 1 фильм");
    }

    @Test
    public void filmControllerGetFilmsTest() throws ValidateException {
        Film film = new Film();
        film.setId(1);
        film.setName("Example");
        film.setDescription("desc Example");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(Duration.ofHours(1));

        Film film2 = new Film();
        film2.setId(2);
        film2.setName("Example2");
        film2.setDescription("desc Example2");
        film2.setReleaseDate(LocalDate.of(2002, 1, 1));
        film2.setDuration(Duration.ofHours(1));

        filmController.createFilm(film);
        filmController.createFilm(film2);
        String[] films = filmController.getFilms().split("\n");

        Assertions.assertEquals(films.length, 2, "ожидается - получено 2 фильма");
    }
}
