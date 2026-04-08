package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class FilmController implements FilmStorage {

    public InMemoryFilmStorage filmStorage;
    public FilmService filmService;
    public InMemoryUserStorage memoryUserStorage;


    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService, InMemoryUserStorage memoryUserStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.memoryUserStorage = memoryUserStorage;
    }

    @Override
    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        return filmStorage.create(film);
    }

    @Override
    @PutMapping("/films")
    public Film update(@RequestBody Film updatedFilm) {
        if (updatedFilm == null || filmStorage.getFilms().containsKey(updatedFilm.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        return filmStorage.update(updatedFilm);
    }

    @Override
    @GetMapping("/films")
    public List<Film> filmAll() {
        return filmStorage.filmAll();
    }

    @Override
    public void clearFilm() {
        filmStorage.clearFilm();
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

    @PutMapping("/films/{filmsId}/like/{id}")
    public boolean addLike(@PathVariable long filmsId, @PathVariable long id) {
        if (filmStorage.getFilms().get(filmsId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        if (memoryUserStorage.getUsers().get(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return filmService.addLike(filmsId, id);
    }


    @DeleteMapping("/films/{filmsId}/like/{id}")
    public boolean removeLikeFilm(@PathVariable long filmsId, @PathVariable long id) {
        if (filmStorage.getFilms().get(filmsId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        if (memoryUserStorage.getUsers().get(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return filmService.removeLike(filmsId, id);
    }

    @GetMapping("/films/popular")
    public List<Film> topFilm(@RequestParam(value = "count", defaultValue = "10") int count) {
        return filmService.getTopFilms(count);
    }

}
