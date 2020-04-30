package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public GameServer() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35566);
           while(true) {
            Socket clientSocket = serverSocket.accept();
            ClientThread clientThreadExecuter = new ClientThread(clientSocket);
            new Thread(clientThreadExecuter).start();
           }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }
}
