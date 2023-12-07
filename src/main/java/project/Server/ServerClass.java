package project.Server;

import java.io.*;
import java.net.*;

public class ServerClass {

    int port;
    ServerSocket server;
    Socket client;
    BufferedReader in;
    PrintWriter out;
    ClientHandler handler;

    public ServerClass(int port) {

        this.port = port;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler = new ClientHandler();
    }

    public Socket connect() {

        try {
            for (;;) {
                System.out.println("Waiting for connection...");
                client = server.accept();
                new PrintWriter(client.getOutputStream(), true);

                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);

                System.out.println("Client connesso: " + client.getInetAddress() + ":" + port);
                ServerThread serverThread = new ServerThread(client, in, out, handler);
                serverThread.start();
            }
        } catch (IOException e) {

            System.out.println("Something went wrong!");
        }

        return client;
    }

}