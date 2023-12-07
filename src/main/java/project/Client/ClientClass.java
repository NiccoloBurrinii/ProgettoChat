package project.Client;

import java.io.*;
import java.net.*;

public class ClientClass {

    int serverPort;
    String serverAddress;

    Socket server;
    BufferedReader in;
    PrintWriter out;
    ClientThreadIn clientThreadIn;
    ClientThreadOut clientThreadOut;

    public ClientClass(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public Socket connect(){

        try {
            server = new Socket(serverAddress, serverPort);

            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            out = new PrintWriter(server.getOutputStream(), true);

            clientThreadIn = new ClientThreadIn(server, in);
            clientThreadOut = new ClientThreadOut(server, out, clientThreadIn);
            clientThreadIn.start();
            clientThreadOut.start();

            System.out.println("Connesso al server: " + serverAddress + ":" + serverPort);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Something went wrong, closing client...");
            System.exit(1);
        }

        return server;
    }

    public void closeConnection(){

        clientThreadIn.closeConnection();
        clientThreadOut.closeConnection();
    }

}
