package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.enums.FriendshipStatus;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    public UserStorage userStorage;
    public UserService userService;
    public JdbcTemplate jdbcTemplate;
    public UserRowMapper userRowMapper;

    @Autowired
    public UserController(@Qualifier("UserDbStorage") UserStorage userStorage, UserService userService, JdbcTemplate jdbcTemplate,
                          UserRowMapper userRowMapper) {
        this.userStorage = userStorage;
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        userService.checkValidationUser(user);
        user = userStorage.create(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User updateUser) {

        userService.checkValidationUser(updateUser);
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
    public List<User> getFriends(@PathVariable("id") long id) {

        if (!userStorage.checkUser(id)) {
            throw new NotFoundException("Пользователь не найден");
        }

        String sql = """
                SELECT u.*
                FROM users AS u
                JOIN friendships AS f ON u.id = f.friendId
                WHERE f.userId = ?
                ORDER BY u.id
                """;

        return jdbcTemplate.query(sql, userRowMapper, id);
    }

    @PutMapping("/users/{id}/friends/{friend_id}")
    public ResponseEntity<Void> addFriend(@PathVariable("id") long id, @PathVariable("friend_id") long friendId) {

        if (!userStorage.checkUser(friendId)) {
            throw new NotFoundException("Пользователь не найден");
        }

        String sql = "INSERT INTO friendships (userId, friendId, status) VALUES (?,?,?)";

        jdbcTemplate.update(sql, id, friendId, FriendshipStatus.CONFIRMED.name());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id, @PathVariable long friendId) {

        if (!userStorage.checkUser(friendId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!userStorage.checkUser(id)) {
            throw new NotFoundException("Пользователь не найден");
        }

        String sql = "DELETE FROM friendships WHERE userId = ? AND friendId = ?";

        jdbcTemplate.update(sql, id, friendId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable long id, @PathVariable long otherId) {

        String sql = "SELECT u.* " +
                "FROM users AS u " +
                "JOIN friendships AS f1 on u.id = f1.friendId " +
                "JOIN friendships AS f2 on u.id = f2.friendId " +
                "WHERE f1.userId = ?" +
                "AND f2.userId = ?" +
                "ORDER BY u.id";

        return jdbcTemplate.query(sql, userRowMapper, id, otherId);
    }


}