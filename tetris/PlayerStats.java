package com.example.tetris;

public class PlayerStats {
    public int score;
    public int level;
    public int speedInMilli;
    public int linesCleared;

    PlayerStats(){
        score=0;
        level=1;
        linesCleared=0;
        speedInMilli=600;
    }

    public void calculatePoints(int numLines){
        switch (numLines) {
            case 1 -> {
                score += (100 * level);
                linesCleared++;
            }
            case 2 -> {
                score += (300 * level);
                linesCleared += 2;
            }
            case 3 -> {
                score += (500 * level);
                linesCleared += 3;
            }
            case 4 -> {
                score += (800 * level);
                linesCleared += 4;
            }
        }

        if(linesCleared>=8){
            if(level<=15){
                level++;
                speedInMilli-=45;
            }
            linesCleared-=5;
        }
    }

    public int getScore() {
        return score;
    }

    public int getLevel(){
        return level;
    }

    public int getSpeedInMilli() {
        return speedInMilli;
    }

    public int getLinesCleared() {
        return linesCleared;
    }


}
