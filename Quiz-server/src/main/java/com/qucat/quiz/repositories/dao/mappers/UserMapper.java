package com.qucat.quiz.repositories.dao.mappers;

import com.qucat.quiz.repositories.entities.Role;
import com.qucat.quiz.repositories.entities.User;
import com.qucat.quiz.repositories.entities.UserAccountStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return User.builder()
                .userId(resultSet.getInt("id"))
                .firstName(resultSet.getString("first_name"))
                .secondName(resultSet.getString("second_name"))
                .login(resultSet.getString("login"))
                .mail(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .profile(resultSet.getString("profile"))
                .registrationDate(resultSet.getDate("registered_date"))
                .score(resultSet.getInt("total_score"))
                .status(UserAccountStatus.valueOf(resultSet.getString("status").toUpperCase()))
                .role(Role.valueOf(resultSet.getString("role").toUpperCase()))
                .build();
    }
}
