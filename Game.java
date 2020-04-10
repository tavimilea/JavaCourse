package com.company;

public class Game {
    private Board gameBoard;
    private Player[] players;
    private int numberOfPlayers;
    private Thread[] threads;
    private GameKeeper gameKeeper;
    private Thread gameMonitor;

    Game(int playersCnt, int tokensCount, String[] names) {
        players = new Player[playersCnt];
        gameBoard = new Board(tokensCount, true, playersCnt,   2);
        this.numberOfPlayers = playersCnt;
        for(int i = 0; i < numberOfPlayers; i ++) {
            switch (i) {
                case 0: players[i] = new SmartPlayer(gameBoard, names[i], 10, this, i); break;
                case 1: players[i] = new ManualPlayer(gameBoard, names[i], 10, this, i); break;
                case 2: players[i] = new RandomPlayer(gameBoard, names[i], 10, this, i); break;
                default: players[i] = new RandomPlayer(gameBoard, names[i], 10, this, i); break;
            }

        }
        threads = new Thread[numberOfPlayers];
        gameKeeper = new GameKeeper(100, this);
        gameMonitor = new Thread(gameKeeper);
        gameMonitor.start();
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    /**
 * Runs on main thread, starts players threads and monitors game state.
 * */
    public void play() {

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
                                gameKeeper.signalFinish();
                                System.out.println("game eded, runtime is:" + gameKeeper.getRuntimeMilis() + " milisec");
                                break;
                            }
                        }
                    }
                    //lock gameBoard to check if empty
                    synchronized (gameBoard) {
                        if (!didPlayerFinished && gameBoard.getAllTokens().length == 0) {
                            //if no player finished, but board empty, lock players to set their score and print them
                            synchronized (players) {
                                for (int i = 0; i < players.length; i++) {
                                    players[i].setPoints(50);
                                    System.out.println("all players won, " + players[i].toString() + "last progresion found");
                                    System.out.println(players[i].progressionFound);
                                    didPlayerFinished = true;
                                    gameKeeper.signalFinish();
                                    System.out.println("game eded, runtime is:" + gameKeeper.getRuntimeMilis() + " milisec");
                                }
                            }
                        }
                    }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopGame() {
            for (int i = 0; i < players.length; i++) {
                players[i].signalStop();
            }
    }
}
