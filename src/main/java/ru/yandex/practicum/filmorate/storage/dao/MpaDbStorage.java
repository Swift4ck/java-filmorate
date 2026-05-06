package ru.yandex.practicum.filmorate.storage.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.inter.MpaStorage;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;

import java.util.List;

@Component
@Qualifier("MpaDbStorage")
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM mpaFilm", mpaRowMapper);
    }

    @Override
    public Mpa getById(long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM mpaFilm WHERE id = ?",
                    new Object[]{id},
                    mpaRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Id mpa не существует");
        }
    }


}
