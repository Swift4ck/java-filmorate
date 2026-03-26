package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDate;


@Data
@AllArgsConstructor
public class Film {
    private int id;
    private String name;
    private String description; //описание
    private LocalDate releaseDate;
    private int duration; //продолжительность
}
