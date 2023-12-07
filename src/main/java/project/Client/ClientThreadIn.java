package project.Client;

import java.io.*;
import java.net.*;

public class ClientThreadIn extends Thread {

    Socket client;
    BufferedReader in;

    public ClientThreadIn(Socket client, BufferedReader in){

        this.client = client; 
        this.in = in;
    }

    @Override
    public void run() {

        try {
            communicate();
        } catch (Exception e) {

        }
    }

    public void communicate() {
            
        String str = "";
        try {
            for(;;) {
                str = in.readLine();
                if(str == null)
                    break;
                System.out.println(str);
            }
        
        } catch (IOException e) {
            System.out.println("Input exception");
        }
    }

    public void closeConnection(){

        try {
            in.close();
        } catch (Exception e) {
            System.out.println("Input exception");
        }
    }

}
