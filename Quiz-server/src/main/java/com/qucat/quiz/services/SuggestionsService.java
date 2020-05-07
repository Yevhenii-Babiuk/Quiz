package com.qucat.quiz.services;

import com.qucat.quiz.repositories.dao.SuggestionDao;
import com.qucat.quiz.repositories.entities.Lang;
import com.qucat.quiz.repositories.entities.MessageInfo;
import com.qucat.quiz.utils.SuggestionsMailingThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@PropertySource("classpath:mail/application-mail-config.properties")
public class SuggestionsService {
    @Autowired
    private EmailSender emailSender;

    @Autowired
    private SuggestionDao suggestionDao;

    @Value("${url}")
    private String URL;

    public void sendSuggestion(int quizId, String quizName, String categoryName) {
        Map<String, String> users = suggestionDao.getLoginAndEmail(quizId);
        if (users.isEmpty()) {
            log.warn("There are no users to recommend quiz with id={}", quizId);
        } else {
            ExecutorService executorService = Executors.newCachedThreadPool();
            for (Map.Entry entry : users.entrySet()) {
                executorService.execute(new SuggestionsMailingThread(emailSender,
                        entry.getKey().toString(), entry.getValue().toString(), URL, quizName, categoryName,
                        Integer.toString(quizId),
                        MessageInfo.suggestion.findByLang(Lang.UA)));
            }
            executorService.shutdown();
        }
        //todo get Lang
    }
}
