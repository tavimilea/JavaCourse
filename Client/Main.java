package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        GameClient client = new GameClient("127.0.0.1", 35566);
        client.connect();
        client.startListeningForRequestInput();
    }
}
