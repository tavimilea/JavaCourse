package com.company;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GameKeeper implements Runnable {
    private boolean finishSignaled = false;
    private double timeToLiveSec;
    private Game monitoringGame;
    private long initialTimeStamp;
    GameKeeper(double timeToLiveSeconds, Game monitoringGame) {
        this.timeToLiveSec = timeToLiveSeconds;
        this.monitoringGame = monitoringGame;
    }

    @Override
    public void run() {
        initialTimeStamp = new Date().getTime() / 1000L ;
        while( (new Date().getTime() / 1000L) - initialTimeStamp < timeToLiveSec && !finishSignaled) {
           try {
               //placed this here bc the execution needs to be delayed, else unexpected behaviour
               TimeUnit.MILLISECONDS.sleep(1);
           } catch (Exception ex) {
               ex.printStackTrace();
           }
        }

        if((new Date().getTime() / 1000L)  - initialTimeStamp >= timeToLiveSec) {
            System.out.println("Time to live expired, stopping game");
            monitoringGame.stopGame();
        } else {
            return;
        }

    }

    public synchronized void signalFinish() {

            this.finishSignaled = true;

    }

    public synchronized long getRuntimeMilis() {
        return (new Date().getTime()) - initialTimeStamp * 1000L;
    }
}
