package ru.yandex.practicum.filmorate.storage.inter;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {

  Collection<Genre> getGenres(); //возвращает список объектов, содержащих жанр;

  Genre getByIdGenre (long id); //возвращает объект, содержащий жанр, с идентификатором id.

}
