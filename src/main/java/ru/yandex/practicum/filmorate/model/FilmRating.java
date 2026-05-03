package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.enums.FilmRatingMPAA;

@Data
public class FilmRating {
    private long id;
    private FilmRatingMPAA name;

    public FilmRating(long id, FilmRatingMPAA name){
        this.id = id;
        this.name = name;
    }

    public FilmRating(){

    }



}
