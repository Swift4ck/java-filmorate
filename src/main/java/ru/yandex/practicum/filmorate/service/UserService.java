package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.enums.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {


    private final UserStorage memoryUserStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage memoryUserStorage) {
        this.memoryUserStorage = memoryUserStorage;
    }

    public User addFriends(User user, User addUser) {
        if (user.getFriendsList().contains(addUser.getId())) {
            log.debug("Пользователь уже добавлен в друзья");
        }

        if (user.getId() == addUser.getId()) {
            log.debug("Нельзя добавть себя в друзья");
        }

        user.getFriendsList().add(new Friendship(user.getId(), addUser.getId(),
                FriendshipStatus.CONFIRMED, LocalDateTime.now()));
        addUser.getFriendsList().add(new Friendship(addUser.getId(), user.getId(),
                FriendshipStatus.CONFIRMED, LocalDateTime.now()));

        return user;
    }

    public boolean deleteFriends(User user, User deleteUser) {
        if (!user.getFriendsList().contains(deleteUser.getId())) {
            log.debug("Пользователя нет в друзьях");
            return false;
        }
        user.getFriendsList().remove(deleteUser.getId());
        deleteUser.getFriendsList().remove(user.getId());


        return true;
    }

    public Set<Friendship> commonFriends(User user1, User user2) {

        Set<Friendship> friendsList1 = user1.getFriendsList();
        Set<Friendship> friendsList2 = user2.getFriendsList();

        Set<Friendship> commonFriends = new HashSet<>(friendsList1);

        commonFriends.retainAll(friendsList2);

        return commonFriends;
    }

    public Set<Friendship> allFriends(long userId) {
        User user = memoryUserStorage.getUsers().get(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return user.getFriendsList();
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

}