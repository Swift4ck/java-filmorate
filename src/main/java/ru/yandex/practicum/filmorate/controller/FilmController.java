package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.inter.FilmStorage;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final UserStorage memoryUserStorage;


    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService,
                          UserStorage memoryUserStorage) {

        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.memoryUserStorage = memoryUserStorage;
    }


    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        filmStorage.checkValidationFilm(film);
        return filmStorage.create(film);
    }


    @PutMapping("/films")
    public Film update(@RequestBody Film updatedFilm) {
        if (updatedFilm == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return filmStorage.update(updatedFilm);
    }


    @GetMapping("/films")
    public List<Film> filmAll() {
        return filmStorage.filmAll();
    }


    public void clearFilm() {
        filmStorage.clearFilm();
    }


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
        return filmStorage.getFilms();
    }

    @PutMapping("/films/{filmsId}/like/{id}")
    public boolean addLike(@PathVariable long filmsId, @PathVariable long id) {
        if (filmStorage.getFilms().get(filmsId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (memoryUserStorage.getUsers().get(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return filmService.addLike(filmsId, id);
    }


    @DeleteMapping("/films/{filmsId}/like/{id}")
    public boolean removeLikeFilm(@PathVariable long filmsId, @PathVariable long id) {
        if (filmStorage.getFilms().get(filmsId) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (memoryUserStorage.getUsers().get(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return filmService.removeLike(filmsId, id);
    }

    @GetMapping("/films/popular")
    public List<Film> topFilm(@RequestParam(value = "count", defaultValue = "10") int count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("users/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmStorage.getFilmById(id);
    }


}