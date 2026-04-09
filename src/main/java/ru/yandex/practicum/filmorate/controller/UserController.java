package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    public UserStorage userStorage;
    public UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        user = userStorage.create(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User updateUser) {
        updateUser = userStorage.updateUser(updateUser);
        return updateUser;
    }

    @GetMapping("/users")
    public Collection<User> userAll() {
        return userStorage.userAll();
    }

    public void clearUser() {
        userStorage.clearUser();
    }

    public Map<Long, User> getUsers() {
        return userStorage.getUsers();
    }

    public boolean checkValidationUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.debug("Пользователь не ввел почту или email без @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.debug("Пользователь не ввел логин или поставил пробел");
            throw new ValidationException("логин не может быть пустым и содержать пробелы;");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            log.debug("Пользователь не ввел ник, ник теперь такой же как и логин");
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Пользователь ввел не коректную дату");
            throw new ValidationException("дата рождения не может быть в будущем.");
        }
        return true;
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        User user = userStorage.getUsers().get(id);

        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        return user.getFriendsList().stream()
                .map(friendId -> userStorage.getUsers().get(friendId))
                .toList();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friendId);

        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        userService.addFriends(user, friend);
        return user;
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteUser(@PathVariable long id, @PathVariable long friendId) {
        User user = userStorage.getUsers().get(id);
        User deleteUser = userStorage.getUsers().get(friendId);

        if (user == null || deleteUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        userService.deleteFriends(user, deleteUser);
        return user;
    }


    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable long id, @PathVariable long otherId) {
        User user = userStorage.getUsers().get(id);
        User otherUser = userStorage.getUsers().get(otherId);

        if (user == null || otherUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userService.commonFriends(user, otherUser).stream()
                .map(friendId -> userStorage.getUsers().get(friendId))
                .toList();
    }


}