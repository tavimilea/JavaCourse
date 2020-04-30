package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientThread implements Runnable {
    private Socket clientSocket;

    ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while(true) {
                String request = in.readLine();
                Thread.currentThread().sleep(100);
                    System.out.println(request);
                    if(request.equals("exit")) {
                        clientSocket.close();
                        return;
                    } else if(request.equals("stop")){
                        outToClient.println("Server stopped");
                        outToClient.flush();
                        System.exit(0);
                    } else {
                        outToClient.println("Server received the request");
                        outToClient.flush();
                    }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
