package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;
import java.util.Map;


public interface FilmStorage {
    Film create(Film film);

    Film update(Film updatedFilm);

    List<Film> filmAll();

    void clearFilm();

    boolean checkValidationFilm(Film film);

    Map<Long, Film> getFilms();

    Film getFilmById(long id);


}

