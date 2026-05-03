package ru.yandex.practicum.filmorate.dal.mappers;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.FilmRatingMPAA;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRatingRowMapper implements RowMapper<FilmRating> {

    @Override
    public FilmRating mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        String nameString = resultSet.getString("name");
        FilmRatingMPAA filmRatingMPAA = FilmRatingMPAA.valueOf(nameString);
        return new FilmRating(resultSet.getLong("id"), filmRatingMPAA);
    }
}
