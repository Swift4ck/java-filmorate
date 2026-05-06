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
    public FilmController(@Qualifier("FilmDbStorage") FilmStorage filmStorage, FilmService filmService,
                          @Qualifier("UserDbStorage") UserStorage memoryUserStorage) {

        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.memoryUserStorage = memoryUserStorage;
    }


    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        filmStorage.checkValidationFilm(film);
        filmService.checkMpa(film);
        Film filmCreate = filmStorage.create(film);
        return filmStorage.getFilmById(film.getId());
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



    public Map<Long, Film> getFilms() {
        return filmStorage.getFilms();
    }

    @PutMapping("/films/{film_id}/like/{id}")
    public void addLike(@PathVariable("film_id") long films_id, @PathVariable("id") long id) {
        filmService.addLike(films_id, id);
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