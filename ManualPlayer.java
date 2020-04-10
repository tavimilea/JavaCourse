package com.company;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ManualPlayer extends Player {

    ManualPlayer(Board attendingGameBoard, String name, int progessionSize, Game attendingGame, int index) {
        super(attendingGameBoard, name, progessionSize, attendingGame, index);
    }

    @Override
    public void run() {
        while (!foundProgression && !stopSignaled) {
            synchronized (attendingGameBoard) {
                //no more tokens to pick, return
                Token[] availableTokens = attendingGameBoard.getTokens(playerId);
                if(progressionFound.size() == progressionSize) {
                    foundProgression = isProgression((Integer[]) progressionFound.toArray(new Integer[progressionFound.size()]));
                    if(!foundProgression) {
                        progressionFound.clear();
                    }
                } else {
                    System.out.println("YouCanPick any tokens from these, and you need to make a " + progressionSize + "-n progression");
                    printTokensArr(availableTokens);
                    System.out.println("your current picks" + progressionFound.toString());
                    Scanner s = new Scanner(System.in);
                    String pickedNumber = s.next();
                    int pickedValue = Integer.parseInt(pickedNumber);
                    int selectedTokenIndex = Arrays.binarySearch(attendingGameBoard.getTokens(playerId), pickedValue);
                    while(selectedTokenIndex < 0) {
                        System.out.println("token " + pickedNumber + "not available" + ".Enter another token from this list" + attendingGameBoard.getTokens(playerId));
                        pickedNumber = s.next();
                        pickedValue = Integer.parseInt(pickedNumber);

                        selectedTokenIndex = Arrays.binarySearch(attendingGameBoard.getTokens(playerId), pickedValue);
                    }
                    progressionFound.add(availableTokens[selectedTokenIndex].getNumber());
                    attendingGameBoard.removeToken(availableTokens[selectedTokenIndex]);
                    attendingGameBoard.updateTurn();
                }
            }
        }
    }

    private void printTokensArr(Token[] tokens) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < tokens.length; i ++) {
            sb.append(tokens[i].getNumber());
            sb.append(",");
        }
        System.out.println(sb);
    }
}
