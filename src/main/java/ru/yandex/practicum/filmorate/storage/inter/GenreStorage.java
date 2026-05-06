package ru.yandex.practicum.filmorate.storage.inter;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {
    Genre create(Genre genre);

    Optional<Genre> findById(Long id);

    List<Genre> findAllGenres();
}

