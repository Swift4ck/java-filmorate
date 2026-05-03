package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRatingRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.FilmRating;

@Service
@Slf4j
public class FilmRatingService {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Autowired
    public FilmRatingService() {
    }

    public FilmRating getFilmRating(Long id){
        String sql = "SELECT id , FROM FilmRatingMPAA WHERE id = ?";
        return jdbcTemplate.queryForObject(sql , new FilmRatingRowMapper(), id);
    }


}
