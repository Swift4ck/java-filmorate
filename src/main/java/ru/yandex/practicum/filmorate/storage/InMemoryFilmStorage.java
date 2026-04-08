package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Getter
public class InMemoryFilmStorage implements FilmStorage {


    private final Map<Long, Film> films = new HashMap<>();
    private int nexId = 1;

    @Override
    public Film create(Film film) {
        if (!checkValidationFilm(film)) {
            throw new ValidationException("Ошибка валидации");
        }
        film.setId(nexId++);
        log.info("Добавлен фильм: " + film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(@RequestBody Film updatedFilm) {
        if (!films.containsKey(updatedFilm.getId())) {
            log.debug("Введен не существующий id");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм по id не найден");
        }
        Film film = films.get(updatedFilm.getId());

        if (checkValidationFilm(updatedFilm)) {

            film.setName(updatedFilm.getName());
            film.setDescription(updatedFilm.getDescription());
            film.setReleaseDate(updatedFilm.getReleaseDate());
            film.setDuration(updatedFilm.getDuration());
            log.info("Обновлена информацию по фильму: " + film);
        }
        return film;
    }

    @Override
    public List<Film> filmAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void clearFilm() {
        films.clear();
        nexId = 1;
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


}
