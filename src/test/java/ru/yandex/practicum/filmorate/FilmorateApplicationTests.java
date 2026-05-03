package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {

    @Autowired
    private FilmController filmController;

    @Autowired
    private UserController userController;

    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;


    @AfterEach
    void clear() {
        filmController.clearFilm();
        userController.clearUser();
    }

    @Test
    @DisplayName("Успешно создаем фильм")
    void successfulFilmСreation() {
        Film film = new Film(1, "Название фильма", "Описание фильма", LocalDate.of(2025, 3, 24), 60);

        Film createFilm = filmController.create(film);
        assertEquals(createFilm, film, "Успешно создаем фильм");
    }

    @Test
    @DisplayName("Выбрасивыем исключение по причине отсутствия названия")
    void throwExceptionsIfTheNameIsMissing() {
        Film film = new Film(1, "", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> filmController.create(film));
        //Выбросить исключения по причине отсутствия названия
    }

    @Test
    @DisplayName("Выбросить исключения по причине  множества сиволов в описание")
    void throwExceptionsDueToTheNumberOfSiwolsInTheDescription() {
        Film film = new Film(1, "Название фильма", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        String longString = new String(new char[201]).replace('\0', 'X');
        film.setDescription(longString);

        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> filmController.create(film));
        //Выбросить исключения по причине  множества сиволов в описание

    }

    @Test
    @DisplayName("Выбросить исключения по причине некорректной даты")
    void throwExceptionsDueToIncorrectDate() {
        Film film = new Film(1, "Название фильма", "Описание фильма", LocalDate.of(1000, 3, 24), 60);

        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> filmController.create(film));
        //Выбросить исключения по причине некорректной даты
    }

    @Test
    @DisplayName("Выбросить исключения потому что отрицательное  число в продолжительности")
    void throwExceptionsBecauseThereIsANegativeNumberInTheDuration() {
        Film film = new Film(1, "Название фильма", "Описание фильма", LocalDate.of(1000, 3, 24), -2);

        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> filmController.create(film));
        //Выбросить исключения потому что отрицательное  число в продолжительности
    }

    @Test
    @DisplayName("Успешно обновляем фильм фильм")
    void successfullyUpdatingTheMovieMovie() {
        Film film = new Film(1, "Название фильма", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        Film newFilm = new Film(1, "Другое название", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        Film createFilm = filmController.create(film);
        Film updateFilm = filmController.update(newFilm);
        assertEquals(newFilm, film, "Успешно обновляем фильм фильм");
    }

    @Test
    @DisplayName("Выбросить исключения при обновление по причине отсутствия названия")
    void throwExceptionsWhenUpdatingDueToMissingName() {
        Film film = new Film(1, "Название фильма", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        Film createFilm = filmController.create(film);
        Film newFilm = new Film(1, "", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> filmController.update(newFilm));
        //Выбросить исключения при обновление по причине отсутствия названия
    }

    @Test
    @DisplayName("Выбросить исключения при обновление по причине  множества сиволов в описание")
    void throwExceptionsWhenUpdatingDueToTheNumberOfSiwolsInTheDescription() {
        Film film = new Film(1, "Название фильма", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        Film createFilm = filmController.create(film);
        Film newFilm = new Film(1, "Другое название", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        String longString = new String(new char[201]).replace('\0', 'X');
        newFilm.setDescription(longString);

        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> filmController.update(newFilm));
        //Выбросить исключения при обновление по причине  множества сиволов в описание
    }

    @Test
    @DisplayName("Выбросить исключения при обновление по причине некорректной даты")
    void throwExceptionsWhenUpdatingDueToIncorrectDate() {
        Film film = new Film(1, "Название фильма", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        Film createFilm = filmController.create(film);
        Film newFilm = new Film(1, "Другое название", "Описание фильма", LocalDate.of(1000, 3, 24), 60);
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> filmController.update(newFilm));
        //Выбросить исключения при обновление по причине некорректной даты
    }

    @Test
    @DisplayName("Выбросить исключения при обновление по причине отрицательного числа")
    void throwExceptionsWhenUpdatingBecauseOfANegativeNumber() {
        Film film = new Film(1, "Название фильма", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        Film createFilm = filmController.create(film);
        Film newFilm = new Film(1, "Другое название", "Описание фильма", LocalDate.of(2025, 3, 24), -1);
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> filmController.update(newFilm));
        //Выбросить исключения при обновление по причине отрицательного числа
    }

    @Test
    @DisplayName("Успешно получаем все фильмы")
    void successGetAllFilm() {
        Film film = new Film(1, "Название фильма", "Описание фильма", LocalDate.of(2025, 3, 24), 60);
        Film film2 = new Film(2, "Другое название", "Другое описание фильма", LocalDate.of(2020, 1, 1), 70);

        Film createFilm = filmController.create(film);
        Film createFilm2 = filmController.create(film2);

        Collection<Film> allFilm = filmController.filmAll();

        List<Film> checkFilm = Arrays.asList(film, film2);

        List<Film> allFilmList = new ArrayList<>(allFilm);

        assertEquals(checkFilm, allFilmList, "Успешно получаем фильмы");
    }

    @Test
    @DisplayName("Успешно создаем пользователя")
    void successUserСreation() {
        User user = new User(1, "sad@a.ru", "sw2", "qwer", LocalDate.of(2002, 2, 3));

        User createUser = userController.create(user);
        assertEquals(user, createUser, "Успешно создаем пользователя");
    }

    @Test
    @DisplayName("Тесты на валидацию пользователя")
    void verifyingTheValidationOfUserCreation() {
        User user = new User(1, "", "sw2", "qwer", LocalDate.of(2002, 2, 3));
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> userController.create(user));
        //Выбросить исключение по причине пустого email

        user.setEmail("dawda");
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> userController.create(user));
        //Выбросить исключения про причине отсутствие  @

        user.setEmail("pochta@poc.ru");
        user.setLogin("sw 2");
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> userController.create(user));
        // Исключение Пользователь поставил пробел

        user.setLogin("");
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> userController.create(user));
        // Исключение Пользователь оставил пустое поле логина

        user.setLogin("sw4");
        user.setName("");
        User createUser = userController.create(user);
        assertEquals(user.getName(), createUser.getName(), "проверка что ник стал как логин если был пустой");

        user.setBirthday(LocalDate.of(2050, 2, 3));
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> userController.create(user));
        //Выбросить исключение если пользователь ввёл не коректную дату
    }

    @Test
    @DisplayName("Успешно обновляем пользователя")
    void successfullyUpdatingTheUser() {
        User user = new User(1, "sad@a.ru", "sw2", "qwer", LocalDate.of(2002, 2, 3));

        userController.create(user);

        User user2 = new User(1, "sad@a.ru", "sw2", "qwerty", LocalDate.of(2002, 2, 3));

        User updateUser = userController.updateUser(user2);

        assertEquals(user2, user, "Успешно обновляем пользователя");
    }

    @Test
    @DisplayName("Тесты на валидацию при обновления данных пользователя")
    void verifyingTheValidationOfUserUpdate() {
        User user = new User(1, "norm@s.ru", "sw2", "qwer", LocalDate.of(2002, 2, 3));
        inMemoryUserStorage.create(user);
        User userNewEmail = new User(1, "", "sw2", "qwer", LocalDate.of(2002, 2, 3));

        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> userController.updateUser(userNewEmail));
        //Выбросить исключение по причине пустого email


        User userNewEmail2 = new User(1, "rererere", "sw2", "qwer", LocalDate.of(2002, 2, 3));
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> userController.updateUser(userNewEmail2));
        //Выбросить исключения про причине отсутствие  @

        User usernameWithASpace = new User(1, "norm@poc.ru", "sw 2", "qwer", LocalDate.of(2002, 2, 3));
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> userController.updateUser(usernameWithASpace));
        // Исключение Пользователь поставил пробел в логине

        User userNoLogin = new User(1, "norm@poc.ru", "", "qwer", LocalDate.of(2002, 2, 3));
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> userController.updateUser(userNoLogin));
        // Исключение Пользователь оставил пустое поле логина

        User userBirthdaysAreComing = new User(1, "norm@poc.ru", "sweg", "qwer", LocalDate.of(2050, 2, 3));
        assertThrows(ru.yandex.practicum.filmorate.exception.ValidationException.class, () -> userController.updateUser(userBirthdaysAreComing));
        //Выбросить исключение если пользователь ввёл не коректную дату
    }

    @Test
    @DisplayName("Успешно получаем пользователей")
    void successGetAllUsers() {
        User user = new User(1, "norm@s.ru", "sw2", "qwer", LocalDate.of(2002, 2, 3));
        User user2 = new User(2, "bigNorm@s.ru", "sw222s", "qwereasdaw", LocalDate.of(2001, 2, 3));

        User createUser = userController.create(user);
        User createUser2 = userController.create(user2);

        Collection<User> allUsers = userController.userAll();

        List<User> checkFilm = Arrays.asList(user, user2);

        List<User> allFilmList = new ArrayList<>(allUsers);

        assertEquals(checkFilm, allFilmList, "Успешно получаем юзеров");
    }



}
