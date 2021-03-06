package com.qucat.quiz.repositories.dao.implementation;

import com.qucat.quiz.repositories.dao.QuestionDao;
import com.qucat.quiz.repositories.dao.mappers.QuestionMapper;
import com.qucat.quiz.repositories.entities.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:database.properties")
public class QuestionDaoImpl extends GenericDaoImpl<Question> implements QuestionDao {

    @Value("#{${sql.question}}")
    private Map<String, String> questionQueries;

    protected QuestionDaoImpl() {
        super(new QuestionMapper(), TABLE_NAME);
    }


    @Override
    protected String getInsertQuery() {
        return questionQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement, Question question) throws SQLException {
        preparedStatement.setInt(1, question.getQuizId());
        preparedStatement.setString(2, question.getType().name().toLowerCase());
        preparedStatement.setString(3, question.getContent());
        preparedStatement.setInt(4, question.getScore());
        preparedStatement.setInt(5, question.getImageId());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return questionQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(Question question) {
        return new Object[]{question.getQuizId(), question.getType().name().toLowerCase(), question.getContent(),
                question.getScore(), question.getImageId(), question.getId()};
    }

    @Override
    public List<Question> getByQuizId(int id) {
        return jdbcTemplate.query(questionQueries.get("getByQuizId"),
                new Object[]{id}, new QuestionMapper());
    }

    @Override
    public int deleteQuestions(List<Integer> questionId) {
        StringBuilder deleteQuery = new StringBuilder(questionQueries.get("deleteQuestions"));
        List<String> mark = new ArrayList<>();
        for (int i = 0; i < questionId.size(); i++) {
            mark.add("?");
        }
        String insertion = String.join(",", mark);
        deleteQuery.replace(deleteQuery.lastIndexOf("(") + 1,
                deleteQuery.lastIndexOf(")") - 1, insertion);
        int deletedRow = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement(deleteQuery.toString());
            deletedRow = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("error while get connection: " + e.getMessage());
            e.getStackTrace();
        }
        return deletedRow;
    }
}

