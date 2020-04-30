package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.rmi.UnknownHostException;

public class GameClient {
    private String serverAdress;
    private int port;
    private PrintWriter outToServer;
    private BufferedReader serverIn;
    private BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

    public GameClient(String serverAdress, int port) {
        this.serverAdress = serverAdress;
        this.port = port;
    }

    public void connect() {
        try {
            Socket servSocket = new Socket(serverAdress, port);
            serverIn = new BufferedReader(new InputStreamReader(servSocket.getInputStream()));
            outToServer =
                    new PrintWriter(servSocket.getOutputStream(), true);
        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListeningForRequestInput() {
        boolean shouldExit = false;

            try {
                while (!shouldExit) {
                    String command = consoleReader.readLine();
                    outToServer.println(command);
                    String serverResponse = serverIn.readLine();
                    System.out.println(serverResponse);
                    if (command.equals("exit")) {
                        shouldExit = true;
                        return;
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
    }
}
