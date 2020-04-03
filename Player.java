package com.company;

import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {
    private Board attendingGameBoard;
    private Game attendingGame;
    private String name;
    private int points;

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean foundProgression = false;
    public ArrayList<Integer> progressionFound;
    private int progressionSize;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized int getPoints() {
        return points;
    }

    Player(Board attendingGameBoard, String name,int progessionSize, Game attendingGame) {
        this.attendingGameBoard = attendingGameBoard;
        this.name = name;
        this.progressionSize = progessionSize;
        progressionFound = new ArrayList<Integer>();
        this.attendingGame = attendingGame;
    }

    @Override
    public void run() {
        while (!foundProgression) {
            synchronized (attendingGameBoard) {
                if (attendingGameBoard.getTokens().length == 0) {
                    return;
                }
                    Token t = attendingGameBoard.getTokens()[0];
                    attendingGameBoard.removeToken(t);
                    int number = t.getNumber();
                    progressionFound.add(number);
                if(progressionFound.size() == progressionSize && attendingGameBoard.getTokens().length > 0) {
                    foundProgression = isProgression((Integer[]) progressionFound.toArray(new Integer[progressionFound.size()]));
                }
                if (foundProgression) {
                    break;
                } else if(progressionFound.size() == progressionSize) {
                    progressionFound.removeAll(progressionFound);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", points=" + points +
                '}';
    }

    private boolean isProgression(Integer[] numbers) {
        if (numbers.length < 2) {
            return false;
        }
        int lastDifference = numbers[1] - numbers[0];
        for(int i = 2; i < numbers.length; i ++) {
            if (numbers[i] - numbers[i - 1] != lastDifference) {
                return false;
            }
        }
        return true;
    }

}
