package ru.yandex.practicum.filmorate.dal;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmRatingService;
import ru.yandex.practicum.filmorate.storage.inter.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final GenreRowMapper genreRowMapper;
    private final FilmRatingService filmRatingService;

    @Override
    public Film create(Film film) {

        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();


        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, film.getName());
                    stmt.setString(2, film.getDescription());
                    stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                    stmt.setInt(4, film.getDuration());
                    stmt.setLong(5, film.getMpa().getId());
                    return stmt;
                },
                keyHolder
        );

        Long generatedId = (Long) keyHolder.getKey();
        film.setId(generatedId);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String genreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            film.getGenres().forEach(genre -> jdbcTemplate.update(genreSql, generatedId, genre.getId())
            );
        }

        String selectSql = "SELECT f.*, g.id as genre_id FROM films f LEFT JOIN film_genres fg ON f.id = fg.film_id LEFT JOIN genres g ON fg.genre_id = g.id WHERE f.id = ?";
        Film fullFilm = jdbcTemplate.queryForObject(selectSql, new FilmRowMapper(filmRatingService), generatedId);

        return film;
    }


    @Override
    public Film update(Film updatedFilm) {
        String sql = "UPDATE films SET name = ?, description = ?,  release_date = ?, duration = ? WHERE id = ?";

        int update = jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, updatedFilm.getName());
                    stmt.setString(2, updatedFilm.getDescription());
                    stmt.setDate(3, Date.valueOf(updatedFilm.getReleaseDate()));
                    stmt.setInt(4, updatedFilm.getDuration());
                    return stmt;
                }
        );

        if (update == 0) {
            throw new NotFoundException("Фильм по id не найден");
        }
        return updatedFilm;
    }

    @Override
    public List<Film> filmAll() {
        return jdbcTemplate.query("SELECT * FROM films", filmRowMapper);
    }

    @Override
    public void clearFilm() {
        jdbcTemplate.update("DELETE FROM films");
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

    public Map<Long, Film> getFilms() {
        List<Film> films = jdbcTemplate.query(
                "Select * FROM films",
                (rs, rowNum) -> {
                    Film film = new Film();
                    film.setId(rs.getLong("id"));
                    film.setName(rs.getString("name"));
                    film.setDescription(rs.getString("description"));
                    film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                    film.setDuration(rs.getInt("duration"));
                    return film;
                }
        );
        return films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film));
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        Film film = jdbcTemplate.queryForObject(sql, new Object[]{id}, filmRowMapper);
        if (film == null) {
            throw new NotFoundException("Фильм по id не найден");
        }
        return film;
    }


}
