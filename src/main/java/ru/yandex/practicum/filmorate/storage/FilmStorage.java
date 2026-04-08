package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;


public interface FilmStorage {
    Film create(Film film);

    Film update(Film updatedFilm);

    List<Film> filmAll();

    void clearFilm();

    boolean checkValidationFilm(Film film);
}

