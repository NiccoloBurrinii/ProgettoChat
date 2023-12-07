package project.Server;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {

        String RESET = "\u001B[0m";
        String YELLOW = "\u001B[33m";

    ServerSocket server;
    Socket client;
    String str;
    BufferedReader in;
    PrintWriter out;
    ClientHandler handler;

    public ServerThread(Socket client, BufferedReader in, PrintWriter out, ClientHandler handler) throws IOException {

        this.client = client;
        server = new ServerSocket();
        str = "";
        this.in = in;
        this.out = out;
        this.handler = handler;
    }

    @Override
    public void run() {
        //richiesta nome utente solo la prima volta
        boolean x = true;
        try {
            if (x) {
                requestLogIn();
                x = !x;
            }
            receiveMessage();
        } catch (Exception e) {
            System.out.println("Input error");
        }
    }

    public void receiveMessage() {
    //prende mess ricevuto, lo passa a messageHandler() e ritorna in ascolto
        String msg = "";
        try {
            while (!(msg = in.readLine()).isEmpty()) {
                handler.messageHandler(msg, this.getName());
            }
         } catch (IOException e) {
                System.out.println("Client disconnesso");
            }

    }

    public void sendMessage(String msg){
        //invia il messaggio
        try {
            out.println(YELLOW + handler.getTime() + RESET + msg);
        } catch (Exception e) {
            System.out.println("Output error");
        }
    }

    public void closeConnection() {

        System.out.println("Closing connection...");
        try {
            out.close();
            in.close();
            client.close();
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
    }

    public void requestLogIn() throws Exception {
        out.println("Inserire l'username: ");
        String username = "";
        while (!(username = in.readLine().trim()).isEmpty()) {
            if (handler.checkName(username) != -1) {
                out.println("L'username scelto non e' disponibile, sceglierne un altro:");
                continue;
            }

            this.setName(username);
            handler.addClient(this);
            break;
        }
    }
    
    @Override
    public String toString() {
        
        return this.getName();
    }
}
