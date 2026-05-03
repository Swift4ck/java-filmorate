package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.service.FilmRatingService;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    private final FilmRatingService filmRatingService;

    public FilmRowMapper(FilmRatingService filmRatingService) {
        this.filmRatingService = filmRatingService;
    }



    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));

        long idRating = resultSet.getLong("mpa_id");
        FilmRating filmRating = filmRatingService.getFilmRating(idRating);

        film.setMpa(filmRating);

        return film;
    }

}
