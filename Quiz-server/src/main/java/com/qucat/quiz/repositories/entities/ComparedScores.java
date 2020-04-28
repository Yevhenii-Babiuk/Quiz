package com.qucat.quiz.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComparedScores {
    private int id;
    private String name;
    private int score;
    private int userId;
    private int record;
}
