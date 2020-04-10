package com.company;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class SmartPlayer extends Player {
    private List<Token> lastTurnTokens = new ArrayList<Token>();
    class InitialToken {
        int ratio;
        Token initialToken;
    }

    SmartPlayer(Board attendingGameBoard, String name, int progessionSize, Game attendingGame, int index) {
        super(attendingGameBoard, name, progessionSize, attendingGame, index);
    }

    @Override
    public void run() {
        int progresionRatioToFind = 0;
        while (!foundProgression && !stopSignaled) {
            synchronized (attendingGameBoard) {
                //no more tokens to pick, return
                Token[] availableTokens = attendingGameBoard.getTokens(playerId);
                if (availableTokens.length == 0) {
                    return;
                }

                //Check if there is a token which a player might pick in order to win, if there is one, remove it
                boolean foundWinningTokenForOthers = false;
                for (int i = 0; i < attendingGame.getNumberOfPlayers(); i++) {
                    if (i != playerId) {
                        Token winningTokenForOthers = findWiningTokenForPlayerIfAvailable(attendingGameBoard.getOtherPlayersPicks().get(i), availableTokens);
                        if (winningTokenForOthers != null) {
                            attendingGameBoard.removeToken(winningTokenForOthers);
                            foundWinningTokenForOthers = true;
                            break;
                        }
                    }
                }

                //if there is no token that makes others win, if the current picks for this player are empty,
                //try and find a start token, which has the longest progression s.t. there will be more chances to make a full k progression
                //if someone removes tokens randomly
                if (!foundWinningTokenForOthers && progressionFound.size() == 0) {
                    InitialToken initialToken = findInitialTokenToPick(availableTokens);
                    if (initialToken.initialToken != null) {
                        progressionFound.add(initialToken.initialToken.getNumber());
                        progresionRatioToFind = initialToken.ratio;
                        attendingGameBoard.removeToken(initialToken.initialToken);
                    }
                } else if (!foundWinningTokenForOthers && progressionFound.size() == progressionSize) {
                    foundProgression = true;
                    progressionFound.clear();
                    InitialToken initialToken = findInitialTokenToPick(availableTokens);
                    progressionFound.add(initialToken.initialToken.getNumber());
                    progresionRatioToFind = initialToken.ratio;
                    attendingGameBoard.removeToken(initialToken.initialToken);
                } else if (!foundWinningTokenForOthers) {
                    Token pickedtoken = findATokenWithRatio(progressionFound.get(progressionFound.size() - 1), availableTokens, progresionRatioToFind);
                    if (pickedtoken == null) {
                        progressionFound.clear();
                        InitialToken initialToken = findInitialTokenToPick(availableTokens);
                        if (initialToken.initialToken != null) {
                            progressionFound.add(initialToken.initialToken.getNumber());
                            progresionRatioToFind = initialToken.ratio;
                            attendingGameBoard.removeToken(initialToken.initialToken);
                        }
                    } else {
                        attendingGameBoard.removeToken(pickedtoken);
                        progressionFound.add(pickedtoken.getNumber());
                    }
                }
                attendingGameBoard.updateTurn();
            }
        }
    }

    /***
     * Checks if a there is a player that needs only one more token in order to win.
     * @param otherPlayerTokens the tokens that other player picked
     * @param availableTokens tokens that are on th  board at the current time
     * @return winning token if available, null otherwise
     */
    private Token findWiningTokenForPlayerIfAvailable(List<Token> otherPlayerTokens, Token[] availableTokens) {
        Integer[] otherPlayersTokenValues = new Integer[otherPlayerTokens.size()];//[(otherPlayerTokens.size() - 1 > 0 ? otherPlayerTokens.size() - 1 : 0)];
        if(otherPlayerTokens.size() > 0) {
            for (int i = 0; i < otherPlayerTokens.size(); i++) {
                otherPlayersTokenValues[i] = otherPlayerTokens.get(i).getNumber();
            }
            if (!isProgression(otherPlayersTokenValues)) {
                return null;
            } else {
                int progressionRatio = otherPlayersTokenValues[1] - otherPlayersTokenValues[0];
                Arrays.sort(availableTokens);
                Token possibleWinningToken = findATokenWithRatio(availableTokens[(availableTokens.length - 1)].getNumber(), availableTokens, progressionRatio);
                if (possibleWinningToken != null) {
                    return possibleWinningToken;
                }
            }
        }
        return null;
    }

    /***
     * Finds a token in @availableTokens that have a progression ratio equal to @ratio,
     * relative to @relativeTo
     * @param relativeTo
     * @param availableTokens
     * @param ratio
     * @return null if there is no such token, the token otherwise
     */
    private Token findATokenWithRatio(int relativeTo, Token[] availableTokens, int ratio) {
        for(int i = 0; i < availableTokens.length; i ++) {
            if(availableTokens[i].getNumber() - relativeTo == ratio) {
                return availableTokens[i];
            }
        }
        return null;
    }

    private InitialToken findInitialTokenToPick(Token[] availableTokens) {
        Arrays.sort(availableTokens);
        Token initialToken = new Token();
        Token pickedToken;
        Token iterationToken;
        InitialToken tk = new InitialToken();
        int maxLenght = 0;
        for(int ratio = 1; ratio < (availableTokens[availableTokens.length - 1].getNumber() / 2); ratio ++) {
            int progressionSize = 0;
            for(int i = 0; i < availableTokens.length; i++) {
                if(i == 0) {
                    initialToken = availableTokens[0];
                    iterationToken = availableTokens[0];
                }
                iterationToken = findATokenWithRatio(initialToken.getNumber(), availableTokens, ratio);
                progressionSize = 0;
                while(iterationToken != null) {
                    iterationToken = findATokenWithRatio(iterationToken.getNumber(), availableTokens, ratio);
                    progressionSize++;
                }
                if(progressionSize > maxLenght) {
                    maxLenght = progressionSize;
                    tk.initialToken = initialToken;
                    tk.ratio = ratio;
                }
            }
        }
        return tk;
    }

}