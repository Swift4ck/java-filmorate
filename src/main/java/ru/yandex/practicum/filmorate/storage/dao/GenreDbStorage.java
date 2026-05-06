package ru.yandex.practicum.filmorate.storage.dao;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.inter.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Component
@Qualifier("GenreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    @Override
    public Genre create(Genre genre){
        String sql = "INSERT INTO genres (name) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);

        genre.setId(keyHolder.getKey().longValue());
        return genre;
    }


    @Override
    public Optional<Genre> findById(Long id){
            List<Genre> genres = jdbcTemplate.query("SELECT * FROM genres WHERE id = ?", genreRowMapper, id);
            return genres.stream().findFirst();
    }


    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query("SELECT * FROM genres", genreRowMapper);
    }

}
