package com.sfu.Menus;

public class GameResult{

    private String difficultyLevel; // Store difficulty level
    private int score; // Store score
    private double time; // Store time


    public GameResult(String difficulty, int score, double time){

        this.difficultyLevel = difficulty;
        this.score = score;
        this.time = time;

    }

    public String getDifficultyLevel(){
        return this.difficultyLevel;
    }

    public int getScore(){
        return this.score;
    }

    public double getTime(){
        return this.time;
    }



}