package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.inter.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Qualifier("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    @Override
    public Film create(Film film) {

        String sql = "INSERT INTO films (name, description, releaseDate, duration, mpaId)" +
                " VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, film.getName());
                    ps.setString(2, film.getDescription());
                    ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                    ps.setInt(4, film.getDuration());

                    if (film.getMpa() != null) {
                        ps.setLong(5, film.getMpa().getId());
                    } else {
                        ps.setNull(5, java.sql.Types.INTEGER);
                    }


                    return ps;
                },
                keyHolder
        );

        film.setId(keyHolder.getKey().longValue());
        saveGenreFilm(film);
        return film;
    }


    @Override
    public Film update(Film updateFilm) {

        String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, mpaId = ?" +
                "WHERE id = ?";

        jdbcTemplate.update(sql,
                updateFilm.getName(),
                updateFilm.getDescription(),
                Date.valueOf(updateFilm.getReleaseDate()),
                updateFilm.getDuration(),
                updateFilm.getMpa().getId(),
                updateFilm.getId());

        return updateFilm;
    }

    @Override
    public List<Film> filmAll() {
        String sql = "SELECT " +
                "f.id, " +
                "f.name, " +
                "f.description, " +
                "f.releaseDate, " +
                "f.duration, " +
                "f.mpaId, " +
                "mf.name AS mpaName, " +
                "g.genreId, " +
                "g.name AS genreName " +
                "FROM films AS f " +
                "JOIN mpaFilm AS mf ON f.mpaId = mf.id " +
                "LEFT JOIN filmGenres AS fg ON f.id = fg.filmId " +
                "LEFT JOIN genres AS g ON fg.genreId = g.genreId " +
                "ORDER BY f.id, g.genreId";

        List<Film> films = jdbcTemplate.query(sql, filmRowMapper);
        return films;
    }

    @Override
    public void clearFilm() {

    }

    @Override
    public boolean checkValidationFilm(Film film) {
        if (film.getName().isEmpty()) {
            log.debug("Пустое названия фильма");
            throw new ValidationException("название не может быть пустым.");
        }

        if (film.getDescription().length() > 200) {
            log.debug("описания длинное");
            throw new ValidationException("максимальная длина описания — 200 символов.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("не коректная дата");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() < 0) {
            log.debug("продолжительность отрицательное число");
            throw new ValidationException("продолжительность фильма должна быть положительным числом.");
        }
        return true;
    }

    @Override
    public Map<Long, Film> getFilms() {
        Map<Long, Film> filmMap = new HashMap<>();
        return filmMap;
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT f.id, " +
                "f.name, " +
                "f.description, " +
                "f.releaseDate, " +
                "f.duration," +
                "mf.id AS mpaId, " +
                "mf.name AS mpaName, " +
                "g.id AS genreId, " +
                "g.name AS genreName " +
                "FROM films AS f " +
                "LEFT JOIN mpaFilm AS mf ON f.mpaId = mf.id " +
                "LEFT JOIN filmGenres AS fg ON f.id = fg.filmId " +
                "LEFT JOIN genres AS g ON fg.id = g.id " +
                "WHERE f.id = ?";

        Film film = jdbcTemplate.queryForObject(sql, new Object[]{id}, filmRowMapper);
        return film;
    }

    public void saveGenreFilm(Film film) {
        if (film.getGenres() == null) {
            return;
        }
        String sql = "INSERT INTO filmGenres (filmId, id) VALUES  (?,?)";

        jdbcTemplate.batchUpdate(sql, film.getGenres(), film.getGenres().size(),
                (ps, genre) -> {
                    ps.setLong(1, film.getId());
                    ps.setLong(2, genre.getId());
                });
    }


}
