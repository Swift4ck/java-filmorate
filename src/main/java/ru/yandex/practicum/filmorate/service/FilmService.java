package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inter.FilmStorage;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {


    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, @Qualifier("UserDbStorage") UserStorage userStorage,
                       JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }


    public void addLike(Long filmId, Long userId) {
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователя с таким id нет");
        }

        String sql = "INSERT INTO filmLikes (filmId, userId) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void removeLike(long id, long userId) {
        if (!userStorage.checkUser(userId)) {
            throw new NotFoundException("Пользователя с таким id нет");
        }

        String sql = "DELETE FROM filmLikes WHERE filmId = ? AND userId = ?";
        jdbcTemplate.update(sql, id, userId);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getCount(count);
    }

    public void checkMpa(Film film) {
        if (film.getMpa() == null || film.getMpa().getId() == 0) {
            throw new ValidationException("MPA должен быть указан");
        }

        if(film.getMpa().getId() > 5){
            throw new NotFoundException("id Mpa не существует");
        }
    }

    public void checkGenre (Film film){
        for (Genre g: film.getGenres()){
            if (g.getId() > 6){
                throw new NotFoundException("id жанра не существует");
            }
        }
    }


}