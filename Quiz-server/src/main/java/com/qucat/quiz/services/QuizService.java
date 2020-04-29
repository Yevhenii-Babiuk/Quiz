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

    @Autowired
    private ImageService imageService;

    @Transactional
    public boolean createQuiz(Quiz quiz) {
        if (quiz == null) {
            log.info("createQuiz: Quiz is null");
            return false;
        }

        if (quiz.getImageId() == -1) {
            quiz.setImageId(imageService.addImage(null));
        }

        int quizId = quizDao.save(quiz);
        if (quizId == -1) {
            log.info("createQuiz: Quiz isn't saved in data base");
            return false;
        }
        quiz.setId(quizId);

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

        Quiz beforeUpdateQuiz = getQuizById(quiz.getId());
        List<Question> afterUpdateQuestions = quiz.getQuestions();
        List<Question> beforeUpdateQuestions = beforeUpdateQuiz.getQuestions();

        List<Question> toInsert = afterUpdateQuestions;
        List<Question> toDelete = beforeUpdateQuestions;

        for (Question buq : beforeUpdateQuestions) {
            for (Question auq : afterUpdateQuestions) {
                if (buq.getId() == auq.getId() && buq.equals(auq)) {
                    toInsert.remove(auq);
                    toDelete.remove(buq);
                }
            }
        }

        questionService.deleteQuestions(toDelete);
        for (Question question : toInsert) {
            questionService.addQuestion(question);
        }

        addQuizTags(quiz);
        quizDao.update(quiz);
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
