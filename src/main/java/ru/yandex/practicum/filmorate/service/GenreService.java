package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.inter.GenreStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService (@Qualifier("GenreDbStorage") GenreStorage genreStorage){
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getGenres(){
        return genreStorage.getGenres();
    }

    public Genre getByIdGenre (long id){
        return genreStorage.getByIdGenre(id);
    }

}
