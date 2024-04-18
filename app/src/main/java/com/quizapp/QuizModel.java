package com.quizapp;

import java.util.ArrayList;

public class QuizModel {
    public ArrayList<Quiz> quiz;
}

class Quiz{
    public ArrayList<Option> options;
    public String question;
}

class Option{
    public int correct_answer;
    public int isAnswered;
    public String option;
}