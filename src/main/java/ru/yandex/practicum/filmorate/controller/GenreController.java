package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.inter.GenreStorage;

import java.util.Collection;

@Slf4j
@RestController
public class GenreController {

    private final GenreStorage genreStorage;

    public GenreController(@Qualifier("GenreDbStorage") GenreStorage genreStorage){
        this.genreStorage = genreStorage;
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenres(){
        return genreStorage.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getByIdGenre(@PathVariable long id){
        return  genreStorage.getByIdGenre(id);
    }

}
