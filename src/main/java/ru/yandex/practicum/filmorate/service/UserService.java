package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {


    private final InMemoryUserStorage memoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage memoryUserStorage) {
        this.memoryUserStorage = memoryUserStorage;
    }

    public User addFriends(User user, User addUser) {
        if (user.getFriendsList().contains(addUser.getId())) {
            log.debug("Пользователь уже добавлен в друзья");
        }

        user.getFriendsList().add(addUser.getId());
        addUser.getFriendsList().add(user.getId());

        memoryUserStorage.refreshMap(user);
        memoryUserStorage.refreshMap(addUser);
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

    public Set<Long> commonFriends(User user1, User user2) {

        Set<Long> friendsList1 = user1.getFriendsList();
        Set<Long> friendsList2 = user2.getFriendsList();

        Set<Long> commonFriends = new HashSet<>(friendsList1);

        commonFriends.retainAll(friendsList2);

        return commonFriends;
    }

    public Set<Long> allFriends(long userId) {
        User user = memoryUserStorage.getUsers().get(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return user.getFriendsList();
    }

}
