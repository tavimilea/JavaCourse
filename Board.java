package com.company;

import java.util.*;

public class Board {
    private List<Token> tokens;
    private int n;
    private int turn = 0;
    private int playersNumber;
    private int progressionSize;
    private Map<Integer, List<Token>> pickedValues = new HashMap<Integer, List<Token>>();

    private Board(){

    };

    public Board(int tokensCount, boolean isFrom1Ton, int playersNumber, int progressionSize) {
        this.n = n;
        this.playersNumber = playersNumber;
        this.progressionSize = progressionSize;
        tokens = new ArrayList<Token>();
        for (int i = 0; i < tokensCount; i++) {
            tokens.add((isFrom1Ton ? new Token(i + 1) : new Token()));
        }
        for(int i = 0; i < playersNumber; i ++) {
            pickedValues.put(i, new ArrayList<Token>());
        }
    }

    public synchronized Token[] getTokens(int forTurn) {
        while(turn != forTurn) {
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tokens.toArray(new Token[0]);
    }

    public synchronized void updateTurn() {
        turn = (turn + 1) % playersNumber;
        notifyAll();
    }

    public Token[] getAllTokens() {
        return tokens.toArray(new Token[0]);
    }

    public void setTokens(Token[] tokens) {
        this.tokens = Arrays.asList(tokens);
    }

    public synchronized void removeToken(Token t) {
        if(pickedValues.get(turn).size() >= progressionSize - 1 ) {
           pickedValues.get(turn).clear();
        }
        pickedValues.get(turn).add(t);
        tokens.remove(t);
    }

    public synchronized Map<Integer, List<Token>> getOtherPlayersPicks() {
        return this.pickedValues;
    }
}
