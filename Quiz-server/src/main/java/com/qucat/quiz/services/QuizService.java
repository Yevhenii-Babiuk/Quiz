package com.qucat.quiz.services;

import com.qucat.quiz.repositories.dao.QuizDao;
import com.qucat.quiz.repositories.entities.Question;
import com.qucat.quiz.repositories.entities.Quiz;
import com.qucat.quiz.repositories.entities.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class QuizService {
    @Autowired
    private QuizDao quizDao;

    @Autowired
    private TagService tagService;

    @Autowired
    private QuestionService questionService;


    @Transactional
    public boolean createQuiz(Quiz quiz) {
        if (quiz == null) {
            log.info("createQuiz: Quiz is null");
            return false;
        }

        int quizId = quizDao.save(quiz);
        if (quizId == -1) {
            log.info("createQuiz: Quiz isn't saved in data base");
            return false;
        }

        for (Question question : quiz.getQuestions()) {
            question.setQuizId(quizId);
            questionService.addQuestion(question);
        }

        addQuizTags(quiz);

        log.info("createQuiz: Quiz successfully saved");
        return true;
    }

    private void addQuizTags(Quiz quiz) {
        for (Tag tag : quiz.getTags()) {
            int tagId = tagService.addTag(tag);
            if (tagId != -1) {
                quizDao.addTag(quiz.getId(), tagId);
            }
        }
    }

    @Transactional
    public void updateQuiz(Quiz quiz) {
        if (quiz == null) {
            log.info("updateQuiz: Quiz is null");
            return;
        }

        quizDao.update(quiz);
        for (Question question : quiz.getQuestions()) {
            questionService.updateQuestion(question);
        }

        addQuizTags(quiz);
    }

    public Quiz getQuizById(int id) {
        return quizDao.getFullInfo(id);
    }

    public Page<Quiz> showPage(int page, int size, String name, String author,
                               List<String> category, Date[] dates, List<String> tags) {
        Timestamp tMinDate = null;
        Timestamp tMaxDate = null;
        if (dates != null && dates.length == 2) {
            tMinDate = new Timestamp(dates[0].getTime());
            tMaxDate = new Timestamp(dates[1].getTime());
        }

        return quizDao.findAllForPage(
                PageRequest.of(page, size,
                        Sort.Direction.DESC, "id"), name, author, category, tMinDate, tMaxDate, tags);
    }
}
