package com.qucat.quiz.repositories.dao.implementation;

import com.qucat.quiz.repositories.dao.DashboardDao;
import com.qucat.quiz.repositories.dao.mappers.UserMapper;
import com.qucat.quiz.repositories.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:dashboard.properties")
public class DashboardDaoImpl implements DashboardDao {
    @Value("#{${sql.dashboard}}")
    private Map<String, String> dashboardQueries;

    protected DashboardDaoImpl() {
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<User> getTopUsers(int limit) {
        return jdbcTemplate.query(dashboardQueries.get("getTopUsers"), new Object[]{limit}, new UserMapper());
    }

    @Override
    public User getBestUserByQuizId(int quizId) {
        User user;
        try {
            user = jdbcTemplate.queryForObject(dashboardQueries.get("getBestUserByQuizId"),
                    new Object[]{quizId}, new UserMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user;
    }

    @Override
    public List<CategoryStatistics> getStatisticInTheCategory(int id) {
        return jdbcTemplate.query(dashboardQueries.get("getStatisticInTheCategoryById"), new Object[]{id}, (rs, rowNum) ->
                new CategoryStatistics(
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getTimestamp("take_date"),
                        rs.getInt("count")
                ));
    }

    @Override
    public List<Statistics> getPercentOfCorrectAnswers(int id) {
        return jdbcTemplate.query(dashboardQueries.get("getPercentOfCorrectAnswersById"), new Object[]{id}, (rs, rowNum) ->
                new Statistics(
                        rs.getString("name"),
                        rs.getDouble("correct_answers_persentage")
                ));
    }

    @Override
    public BestQuiz getTheMostSuccessfulQuiz(int id) {
        try {
            return jdbcTemplate.queryForObject(dashboardQueries.get("getMaxScoreById"), new Object[]{id}, (rs, rowNum) ->
                    new BestQuiz(
                            rs.getString("name"),
                            rs.getTimestamp("take_date"),
                            rs.getInt("score")
                    ));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<ComparedScores> getComparedScores(int userId) {
        return jdbcTemplate.query(dashboardQueries.get("getComparedScores"), new Object[]{userId}, (rs, rowNum) ->
                new ComparedScores(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("score"),
                        rs.getInt("userId"),
                        rs.getInt("record")
                ));
    }

    @Override
    public List<Statistics> getFriendsPreferences(int userId) {
        return jdbcTemplate.query(dashboardQueries.get("getFriendsPreferences"), new Object[]{userId}, (rs, rowNum) ->
                new Statistics(
                        rs.getString("name"),
                        rs.getDouble("count")
                ));
    }

    @Override
    public List<Statistics> getStatisticOfQuizzesPlayed() {
        return jdbcTemplate.query(dashboardQueries.get("getStatisticOfQuizzesPlayed"), (rs, rowNum) ->
                new Statistics(
                        rs.getString("name"),
                        rs.getDouble("count")
                ));
    }

    @Override
    public List<AdminStatistics> getAmountOfCreatedAndPublishedQuizzes() {
        return jdbcTemplate.query(dashboardQueries.get("getAmountOfPublishedQuizzes"), (rs, rowNum) ->
                new AdminStatistics(
                        rs.getDate("date"),
                        rs.getInt("created"),
                        rs.getInt("published")
                ));
    }

}

