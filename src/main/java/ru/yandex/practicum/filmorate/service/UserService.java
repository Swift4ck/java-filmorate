package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {


    private final UserStorage memoryUserStorage;

    @Autowired
    public UserService(UserStorage memoryUserStorage) {
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

}