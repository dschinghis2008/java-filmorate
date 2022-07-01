package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

@Component
public class DbFilmStorage implements FilmStorage {
    private static final LocalDate LOW_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final JdbcTemplate jdbcTemplate;

    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        String sql = "INSERT INTO films(name,description,releasedate,duration,rate,mpa) "
                + "values(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration()
                , film.getRate(), film.getMpa().getId());
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(
                "SELECT f.id_film id_film,m.name name FROM films f JOIN mpa_rating m ON f.mpa=m.id_rate "
                        + " WHERE f.name=? and f.releasedate=?", film.getName(), film.getReleaseDate()
        );
        if (mpaRow.next()) {
            film.getMpa().setName(mpaRow.getString("name"));
            film.setId(mpaRow.getLong("id_film"));
        }
        try {
            if (film.getGenres() != null || !film.getGenres().isEmpty()) {
                for (Genre genre : film.getGenres()) {
                    sql = "INSERT INTO FILM_GENRE_LINK(ID_GENRE, ID_FILM) VALUES ( ?,? )";
                    jdbcTemplate.update(sql, genre.getId(), film.getId());
                }
            }
        } catch (RuntimeException e) {

        }

        return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (film.getId() <= 0 || film.getId() == null) {
            throw new NotFoundException("id должен быть > 0");
        }
        String sql = "UPDATE films SET name=?,description=?,releasedate=?,duration=?,rate=?,mpa=? WHERE id_film=?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration()
                , film.getRate(), film.getMpa().getId(), film.getId());
        film.setMpa(getMpa(film.getMpa().getId()));
        try {
            if (film.getGenres().size() > 0) {
                sql = "DELETE FROM FILM_GENRE_LINK WHERE ID_FILM=?";
                jdbcTemplate.update(sql, film.getId());
                for (Genre genre : film.getGenres()) {
                    sql = "MERGE INTO FILM_GENRE_LINK (ID_FILM, ID_GENRE) KEY (ID_FILM, ID_GENRE) VALUES (?, ?)";
                    jdbcTemplate.update(sql, film.getId(), genre.getId());
                }
                film.setGenres(getGenres(film));
            }
        } catch (RuntimeException e) {

        }

        return Optional.of(film);
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = new ArrayList<>();
        Mpa mpa = null;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT f.*,m.* FROM films f JOIN mpa_rating m ON m.id_rate=f.mpa");
        while (filmRows.next()) {
            Long idFilm = filmRows.getLong("id_film");
            HashSet<Genre> genres = new HashSet<>();
            mpa = new Mpa(filmRows.getLong("m.id_rate"), filmRows.getString("m.name"));

            Film film = new Film(
                    idFilm,
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releasedate").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getLong("rate")
            );
            film.setGenres(getGenres(film));
            films.add(film);
        }
        return films;
    }

    @Override
    public void deleteFilm(Long id) {
        String sql = "DELETE FROM films WHERE id_film=?;DELETE FROM FILM_GENRE_LINK WHERE ID_FILM=?;";
        jdbcTemplate.update(sql, id, id);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM films;DELETE FROM FILM_GENRE_LINK";
        jdbcTemplate.update(sql);
    }

    @Override
    public Optional<Film> getById(Long id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("фильм не найден");
        }
        Mpa mpa = null;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id_film=?", id);
        if (filmRows.next()) {

            Film film = new Film(
                    filmRows.getLong("id_film"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releasedate").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getLong("rate")
            );
            film.setMpa(getMpa(filmRows.getLong("mpa")));
            film.setGenres(getGenres(film));
            return Optional.of(film);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public void addLike(Long idUser, Long idFilm) {
        String sql = "INSERT INTO rating(id_user,id_film) VALUES(?,?)";
        jdbcTemplate.update(sql, idUser, idFilm);
    }

    @Override
    public void removeLike(Long idUser, Long idFilm) {
        String sql = "DELETE FROM rating WHERE id_user=? AND id_film=?";
        jdbcTemplate.update(sql, idUser, idFilm);
    }

    @Override
    public List<Optional<Film>> getOrderRate(Integer limit) {
        List<Optional<Film>> listRate = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT f.ID_FILM,f.NAME,f.DESCRIPTION,f.DURATION,f.RELEASEDATE,f.RATE,f.MPA,COUNT(r.ID_USER) "
                        + "FROM films f LEFT JOIN RATING r ON f.ID_FILM = r.ID_FILM "
                        + "GROUP BY f.ID_FILM ORDER BY COUNT(r.ID_USER) DESC LIMIT ?"
                , limit);
        while (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("id_film"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releasedate").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getLong("rate")
            );
            film.setMpa(getMpa(filmRows.getLong("mpa")));
            film.setGenres(getGenres(film));
            listRate.add(Optional.of(film));
        }
        return listRate;
    }

    public Mpa getMpa(Long idMpa) {
        SqlRowSet rows = jdbcTemplate.queryForRowSet("SELECT NAME FROM MPA_RATING WHERE ID_RATE=?", idMpa);
        if (rows.next()) {
            return new Mpa(idMpa, rows.getString(1));
        } else {
            return null;
        }
    }

    public HashSet<Genre> getGenres(Film film) {
        HashSet<Genre> genres = new HashSet<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM GENRES g LEFT JOIN FILM_GENRE_LINK fgl on g.ID_GENRE = fgl.ID_GENRE WHERE fgl.ID_FILM=?"
                , film.getId());
        while (rows.next()) {
            genres.add(new Genre(rows.getLong("id_genre"), rows.getString("name")));
        }
        if (genres.size() > 0) {
            return genres;
        } else {
            return null;
        }
    }

}
