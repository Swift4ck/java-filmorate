package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.inter.GenreStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(@Qualifier("GenreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(@PathVariable("id") long id) {
        return genreStorage.findById(id).orElseThrow(() -> new NotFoundException("Genre not found"));
    }

    public List<Genre> getAllGenres(){
        return genreStorage.findAllGenres();
    }

}
