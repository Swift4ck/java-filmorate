package ru.yandex.practicum.filmorate.storage.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public User create(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User updateUser(User updateUser) {
        if (!checkUser(updateUser.getId())) {
            throw new NotFoundException("Пользователя с таким id нет");
        }

        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sql,
                updateUser.getEmail(),
                updateUser.getLogin(),
                updateUser.getName(),
                Date.valueOf(updateUser.getBirthday()),
                updateUser.getId());

        return updateUser;
    }

    @Override
    public Collection<User> userAll() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);

    }

    @Override
    public void clearUser() {
        return;
    }

    @Override
    public boolean checkValidationUser(User user) {
        return true;
    }

    @Override
    public Map<Long, User> getUsers() {
        Map<Long, User> us = new HashMap<>();
        return us;
    }


    @Override
    public boolean checkUser(Long id) {
        String checkSql = "SELECT COUNT(1) FROM users WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }

}
