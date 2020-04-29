package com.qucat.quiz.controllers;

import com.qucat.quiz.repositories.entities.*;
import com.qucat.quiz.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/profile/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/top")
    public List<User> getTopPlayers() {
        return dashboardService.getTopUsers(10);
    }

    @GetMapping("/{id}/quizzes/played/categories")
    public  List<CategoryStatistics> getStatisticInTheCategory (@PathVariable int id) {
        return dashboardService.getStatisticInTheCategory(id);
    }

    @GetMapping("/{id}/quizzes/played/percent")
    public  List<Statistics> getPercentOfCorrectAnswers (@PathVariable int id) {
        return dashboardService.getPercentOfCorrectAnswers(id);
    }

    @GetMapping("/{id}/quizzes/played/compare")
    public  List<ComparedScores> getComparedScores (@PathVariable int id) {
        return dashboardService.getComparedScores(id);
    }

    @GetMapping("/{id}/friends/preferences")
    public  List<Statistics> getFriendsPreferences (@PathVariable int id) {
        return dashboardService.getFriendsPreferences(id);
    }

    @GetMapping("/quizzes/played/amount")
    public  List<Statistics> getStatisticOfQuizzesPlayed () {
        return dashboardService.getStatisticOfQuizzesPlayed();
    }

    @GetMapping("/quizzes/status")
    public  List<AdminStatistics> getAmountOfCreatedAndPublishedQuizzes () {
        return dashboardService.getAmountOfCreatedAndPublishedQuizzes();
    }
}
