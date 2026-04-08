package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface FilmStorage {
    Film create(Film film);
    Film update(Film updatedFilm);
    List<Film> filmAll();
    void clearFilm();
    boolean checkValidationFilm(Film film);
}

