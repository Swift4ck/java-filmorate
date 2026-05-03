package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.inter.GenreStorage;

import java.util.Collection;


@Component
@Qualifier("GenreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapperr;

    @Override
    public Collection<Genre> getGenres(){
        return jdbcTemplate.query("SELECT * FROM film_genres", genreRowMapperr);
    }

    @Override
    public Genre getByIdGenre(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM genres WHERE id = ?",
                new Object[] {id},
                (rs, rowNum) -> {
                    Genre genre = new Genre();
                    genre.setId(rs.getLong("id"));
                    genre.setName(rs.getString("name"));
                    return genre;
                }
        );
    }

}
