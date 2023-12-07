package project.Client;

import java.io.*;
import java.net.*;

public class ClientThreadOut extends Thread {

    int serverPort;
    String serverAddress;

    Socket client;
    PrintWriter out;
    BufferedReader stdIn;
    ClientThreadIn clientThreadIn;

    public ClientThreadOut(Socket client, PrintWriter out, ClientThreadIn clientThreadIn){

        this.client = client; 
        this.out = out;
        this.clientThreadIn = clientThreadIn;
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {

        try {
            communicate();
        } catch (Exception e) {

        }
    }

    public void communicate() {
            
        String userInput;
        try {
            
            for(;;){
                userInput = stdIn.readLine().trim().replaceAll(" +", " ");
                out.println(userInput);
                if(userInput.equals("/leave")){
                    clientThreadIn.closeConnection();
                    closeConnection();
                    break;
                }
            }
        
        } catch (IOException e) {
            System.out.println("Output exception!");
        }
    }

    public void closeConnection(){

        try {
            out.close();
            stdIn.close();
        } catch (Exception e) {
            System.out.println("Output exception");
        }
    }

}
