package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    User create(User user);

    User updateUser(User updateUser);

    Collection<User> userAll();

    void clearUser();

    boolean checkValidationUser(User user);

    Map<Long, User> getUsers();

}
