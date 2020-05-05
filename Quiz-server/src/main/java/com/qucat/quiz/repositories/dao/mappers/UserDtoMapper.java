package com.qucat.quiz.repositories.dao.mappers;

import com.qucat.quiz.repositories.dto.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDtoMapper implements RowMapper<UserDto> {

        @Override
        public UserDto mapRow(ResultSet resultSet, int i) throws SQLException {
            return UserDto.builder()
                    .id(resultSet.getInt("id"))
                    .login(resultSet.getString("login"))
                    .gameId(resultSet.getString("game_id"))
                    .registerId(resultSet.getInt("registered_id"))
                    .score(resultSet.getInt("total_score"))
                    .comboAnswer(resultSet.getInt("combo_answer"))
                    .build();
        }
    }