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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("SELECT f.id_film,m.name FROM films f JOIN mpa_rating m ON f.mpa=m.id_rate "
                + " WHERE f.name=? and f.releasedate=?", film.getName(), film.getReleaseDate());
        if (mpaRow.next()) {
            film.getMpa().setName(mpaRow.getString("name"));
            film.setId(mpaRow.getLong("id_film"));
        }
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                sql = "INSERT INTO FILM_GENRE_LINK(ID_GENRE, ID_FILM) VALUES ( ?,? )";
                jdbcTemplate.update(sql, genre.getId(), film.getId());
            }
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
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                sql = "UPDATE FILM_GENRE_LINK SET ID_GENRE=? WHERE ID_FILM=?";
                jdbcTemplate.update(sql, genre.getId(), film.getId());
            }
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
            List<Genre> genres = new ArrayList<>();
            mpa = new Mpa(filmRows.getLong("m.id_rate"), filmRows.getString("m.name"));
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT G.ID_GENRE id_genre,G.NAME name FROM FILM_GENRE_LINK FGL "
                    + "JOIN GENRES G on G.ID_GENRE = FGL.ID_GENRE WHERE FGL.ID_FILM=?", idFilm);
            while (genreRows.next()) {
                Genre genre = new Genre(genreRows.getLong("id_genre"), genreRows.getString("name"));
                genres.add(genre);
            }
            Film film = new Film(
                    idFilm,
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releasedate").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getLong("rate"),
                    mpa, genres
            );
            films.add(film);
        }
        return films;
    }

    @Override
    public void deleteFilm(Long id) {
        String sql = "DELETE FROM films WHERE id_film=?";
        jdbcTemplate.update(sql, id);
        sql = "DELETE FROM FILM_GENRE_LINK WHERE ID_FILM=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM films";
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
            SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa_rating WHERE id_rate=?", filmRows.getLong("mpa"));
            if (mpaRows.next()) {
                mpa = new Mpa(mpaRows.getLong("id_rate"), mpaRows.getString("name"));
            }
            List<Genre> genres = new ArrayList<>();
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT G.ID_GENRE id_genre,G.NAME name FROM FILM_GENRE_LINK FGL "
                    + "JOIN GENRES G on G.ID_GENRE = FGL.ID_GENRE WHERE FGL.ID_FILM=?", id);
            while (genreRows.next()) {
                Genre genre = new Genre(genreRows.getLong("id_genre"), genreRows.getString("name"));
                genres.add(genre);
            }
            if(genres.size() == 0){
                genres = null;
            }
            Film film = new Film(
                    filmRows.getLong("id_film"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releasedate").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getLong("rate"),
                    mpa,
                    genres
            );
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
    public List<Optional<Film>> getOrderRate() {
        List<Optional<Film>> listRate = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films f WHERE rate>0 ORDER BY f.rate DESC");
        while (filmRows.next()) {
            String s = "";
            Long m = filmRows.getLong("mpa");
            SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa_rating WHERE id_rate=?", m);
            if (mpaRows.next()) {
                m = mpaRows.getLong("id_rate");
                s = mpaRows.getString("name");
            }
            Mpa mpa = new Mpa(m, s);
            Film film = new Film(
                    filmRows.getLong("id_film"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releasedate").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getLong("rate"),
                    mpa, null
            );
            listRate.add(Optional.of(film));
        }
        return listRate;
    }
}
