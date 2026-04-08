package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

     User create(User user);
     User updateUser(User updateUser);
     Collection<User> userAll();
     void clearUser();
    boolean checkValidationUser(User user);

}
