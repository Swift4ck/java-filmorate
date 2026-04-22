package ru.yandex.practicum.filmorate.model;


import lombok.Data;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
public class Film {
    private long id;
    private String name;
    private String description; //описание
    private LocalDate releaseDate;
    private int duration; //продолжительность
    private Set<Long> likes = new HashSet<>();
    private List<Genre> genres;
    private FilmRating filmRating;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
