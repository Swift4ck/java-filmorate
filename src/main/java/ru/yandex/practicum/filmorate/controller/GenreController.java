package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.inter.FilmStorage;
import ru.yandex.practicum.filmorate.storage.inter.GenreStorage;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;

import java.util.Collection;

@Slf4j
@RestController
public class GenreController {

    private final GenreStorage genreStorage;
    private final GenreService genreService;

    @Autowired
    public GenreController(@Qualifier("GenreDbStorage") GenreStorage genreStorage, GenreService genreService) {
        this.genreStorage = genreStorage;
        this.genreService = genreService;
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable("id") long id) {
        return genreService.getGenreById(id);
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenres(){
        return genreService.getAllGenres();
    }

}
