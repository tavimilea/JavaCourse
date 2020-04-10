package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Player implements Runnable {
    protected Board attendingGameBoard;
    protected Game attendingGame;
    protected int playerId;
    protected String name;
    protected int points;
    protected boolean stopSignaled = false;
    public void setPoints(int points) {
        this.points = points;
    }

    public boolean foundProgression = false;
    public ArrayList<Integer> progressionFound;
    protected int progressionSize;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized int getPoints() {
        return points;
    }

    Player(Board attendingGameBoard, String name,int progessionSize, Game attendingGame,int index) {
        this.attendingGameBoard = attendingGameBoard;
        this.name = name;
        this.progressionSize = progessionSize;
        progressionFound = new ArrayList<Integer>();
        this.attendingGame = attendingGame;
        this.playerId = index;
    }

    @Override
    public void run() {
        while (!foundProgression && !stopSignaled) {
            synchronized (attendingGameBoard) {
                //no more tokens to pick, return
                if (attendingGameBoard.getTokens(playerId).length == 0) {
                    return;
                }
                Random rand = new Random();
                //get a random token
                Token t = attendingGameBoard.getTokens(playerId)[rand.nextInt(attendingGameBoard.getTokens((playerId)).length)];
                attendingGameBoard.removeToken(t);
                int number = t.getNumber();

                //add nuber to progression
                progressionFound.add(number);

                //the progression lenght reached, check if it is progression
                if(progressionFound.size() == progressionSize && attendingGameBoard.getTokens(playerId).length > 0) {
                    foundProgression = isProgression((Integer[]) progressionFound.toArray(new Integer[progressionFound.size()]));
                }
                //if progresion found, return
                if (foundProgression) {
                    break;
                } else if(progressionFound.size() == progressionSize) {
                    // else remove all numbers found, and try again
                    progressionFound.removeAll(progressionFound);
                }
                attendingGameBoard.updateTurn();
            }
        }
    }

    public synchronized void signalStop() {
        stopSignaled = true;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", points=" + points +
                '}';
    }

    protected boolean isProgression(Integer[] numbers) {
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
