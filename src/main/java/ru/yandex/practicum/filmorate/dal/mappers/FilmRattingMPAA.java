package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.enums.FilmRatingMPAA;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRattingMPAA implements RowMapper<FilmRatingMPAA> {

    @Override
    public FilmRatingMPAA mapRow(ResultSet resultSet, int rowNum) throws SQLException{
        String name = resultSet.getString("name");
        return FilmRatingMPAA.valueOf(name);
    }
}
