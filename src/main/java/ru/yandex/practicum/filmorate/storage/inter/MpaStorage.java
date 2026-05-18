package ru.yandex.practicum.filmorate.storage.inter;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    List<Mpa> getAllMpa();

    Mpa getById(long id);

}
