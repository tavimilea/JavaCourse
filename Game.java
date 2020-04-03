package com.company;

public class Game {
    private Board gameBoard;
    private Player[] players;
    private int numberOfPlayers;
    private Thread[] threads;

    Game(int playersCnt, int tokensCount, String[] names) {
        players = new Player[playersCnt];
        gameBoard = new Board(tokensCount, true);
        this.numberOfPlayers = playersCnt;
        for(int i = 0; i < numberOfPlayers; i ++) {
            players[i] = new Player(gameBoard, names[i], 200, this);
        }
        threads = new Thread[numberOfPlayers];
    }
/**
 * Runs on main thread, starts players threads and monitors game state.
 * */
    public  void play() {

        for(int i = 0; i < players.length; i++) {
        threads[i] = new Thread(players[i]);
        threads[i].start();
        }
        try {
            boolean didPlayerFinished = false;
                while (!didPlayerFinished) {
                    //lock players to check the foundProgression flag
                    synchronized (players) {
                        for (int i = 0; i < players.length; i++) {
                            if (players[i].foundProgression) {
                                players[i].setPoints(100);
                                System.out.println(players[i].toString() + "won");
                                System.out.println(players[i].progressionFound.toString() + players[i].toString());
                                didPlayerFinished = true;
                                break;
                            }
                        }
                    }
                    //lock gameBoard to check if empty
                    synchronized (gameBoard) {
                        if (!didPlayerFinished && gameBoard.getTokens().length == 0) {
                            //if no player finished, but board empty, lock players to set their score and print them
                            synchronized (players) {
                                for (int i = 0; i < players.length; i++) {
                                    players[i].setPoints(50);
                                    System.out.println("all players won, " + players[i].toString() + "last progresion found");
                                    System.out.println(players[i].progressionFound);
                                    didPlayerFinished = true;
                                }
                            }
                        }
                    }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
