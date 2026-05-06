package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
public class Film {
    private long id;
    private String name;
    private String description; //описание
    private LocalDate releaseDate;
    private int duration; //продолжительность
    private Set<Long> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>(); //жанры
    private Mpa mpa; //возрастной рейтинг

    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.genres = new HashSet<>();
    }

    public Film(long id, String name, String description, LocalDate releaseDate, int duration,  Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.genres = new HashSet<>();
        this.mpa = mpa;
    }


    public Film() {

    }

}
