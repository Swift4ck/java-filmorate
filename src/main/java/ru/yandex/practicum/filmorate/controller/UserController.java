package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping("/users")
    public User create(@PathVariable("id") int id,@RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.debug("Пользователь не ввел почту или email без @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.debug("Пользователь не ввел логин или поставил пробел");
            throw new ValidationException("логин не может быть пустым и содержать пробелы;");
        }

        if (user.getName().isEmpty()) {
            log.debug("Пользователь не ввел ник, ник теперь такой же как и логин");
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Пользователь ввел не коректную дату");
            throw new ValidationException("дата рождения не может быть в будущем.");
        }

        log.info("Пользователь создан" + user);
        users.put(user.getId(),user);
        return user;
    }

    @PutMapping("/user/{id}")
    public User update(@PathVariable("id") int id, @RequestBody User updateUser) {
        User user = users.get(id);

        if (user == null) {
            throw new ValidationException("Пользователь с таким ID не найден.");
        }

        if (updateUser == null) {
            log.debug("Введен не существующий id");
            throw new ValidationException("Пользователь с таким ID не найден.");
        }

        if (updateUser.getEmail() == null || !updateUser.getEmail().contains("@")) {
            log.debug("Пользователь не ввел почту или email без @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }

        if (updateUser.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.debug("Пользователь не ввел логин или поставил пробел");
            throw new ValidationException("логин не может быть пустым и содержать пробелы;");
        }

        if (updateUser.getName().isEmpty()) {
            log.debug("Пользователь не ввел ник, ник теперь такой же как и логин");
            user.setName(user.getLogin());
        }

        if (updateUser.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Пользователь ввел не коректную дату");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

        user.setEmail(updateUser.getEmail());
        user.setLogin(updateUser.getLogin());
        user.setName(updateUser.getName());
        user.setBirthday(updateUser.getBirthday());

        log.info("Пользователь обновил данные");
        return user;
    }

    @GetMapping("/users")
    public Collection<User> userAll() {
        return users.values();
    }


}
