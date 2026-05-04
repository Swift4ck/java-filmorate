package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {


    private final Map<Long, User> users = new HashMap<>();
    private int nexId = 1;

    @Override
    public User create(@RequestBody User user) {
        if (checkValidationUser(user)) {
            user.setId(nexId++);
            log.info("Пользователь создан; " + user);
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User updateUser(@RequestBody User updateUser) {
        if (!users.containsKey(updateUser.getId())) {
            log.debug("Введён несуществующий id");
            throw new NotFoundException("Пользователь по id не найден");
        }

        if (!checkValidationUser(updateUser)) {
            throw new ValidationException("Данные пользователя не прошли валидацию");
        }
        User user = users.get(updateUser.getId());

        user.setEmail(updateUser.getEmail());
        user.setLogin(updateUser.getLogin());
        user.setName(updateUser.getName());
        user.setBirthday(updateUser.getBirthday());

        log.info("Пользователь обновил данные");
        return user;
    }


    @Override
    public Collection<User> userAll() {
        return users.values();
    }

    @Override
    public void clearUser() {
        users.clear();
        nexId = 1;
    }


    @Override
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

    public void refreshMap(User user) {
        users.put(user.getId(), user);
        log.info("Пользователь сохранён в хранилище:");
    }


}