package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private List<Token> tokens;
    private int n;
    private Board(){

    };

    public Board(int tokensCount, boolean isFrom1Ton) {
        this.n = n;
        tokens = new ArrayList<Token>();
            for(int i = 0; i < tokensCount; i ++) {
                tokens.add((isFrom1Ton ? new Token(i+1) : new Token()));
            }
    }

    public Token[] getTokens() {
        return tokens.toArray(new Token[0]);
    }

    public void setTokens(Token[] tokens) {
        this.tokens = Arrays.asList(tokens);
    }

    public synchronized void removeToken(Token t) {
        tokens.remove(t);
    }
}
