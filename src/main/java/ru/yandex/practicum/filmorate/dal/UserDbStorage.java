package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@Qualifier("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (email , login, name ,birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, user.getEmail());
                    stmt.setString(2, user.getLogin());
                    stmt.setString(3, user.getName());
                    stmt.setDate(4, Date.valueOf(user.getBirthday()));
                    return stmt;
                },
                keyHolder
        );
        Long generatedId = (Long) keyHolder.getKey();
        user.setId(generatedId);

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

        int updated = jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, user.getEmail());
                    stmt.setString(2, user.getLogin());
                    stmt.setString(3, user.getName());
                    stmt.setDate(4, Date.valueOf(user.getBirthday()));
                    stmt.setLong(5, user.getId());
                    return stmt;
                }
        );
        if (updated == 0) {
            throw new NotFoundException("Пользователь по id не найден");
        }

        return user;
    }


    @Override
    public Collection<User> userAll() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    @Override
    public void clearUser() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @Override
    public boolean checkValidationUser(User user) {
        return true;
    }

    @Override
    public Map<Long, User> getUsers() {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users",
                (rs, rowNum) -> {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setEmail(rs.getString("email"));
                    user.setLogin(rs.getString("login"));
                    user.setName(rs.getString("name"));
                    user.setBirthday(rs.getDate("birthday").toLocalDate());
                    return user;
                }
        );

        return users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    }


}
