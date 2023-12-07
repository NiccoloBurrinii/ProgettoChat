package project.Server;

import java.util.ArrayList;
import java.time.LocalTime;

public class ClientHandler {

        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String BLUE = "\u001B[34m";
        String server = "Server";

    ArrayList<ServerThread> clients = new ArrayList<>();
    private String[] helpList = { "--Lista dei comandi--",
            "   /setname <username> - imposta un nuovo nome",
            "   /tell <username> <msg> - invia un messaggio privato ad un solo utente (!ATTENZIONE! il server potra' comunque leggere il messaggio)",
            "   /leave - disconnetti dalla chatroom",
            "   /userlist - mostra la lista di utenti connessi",
            "   /help - mostra questa lista" };

    public void addClient(ServerThread newClient) {

        clients.add(newClient);
        sendToAll(newClient.getName() + " si e' connesso!", server);
        sendPrivate("Benvenuto " + newClient.getName() + "!", server, newClient.getName());
        sendPrivate("Digita /help per una lista di comandi", server, newClient.getName());
        sendClientList();
    }

    public void messageHandler(String msg, String username) {

        //interpreta e invia il messaggio
        if(String.valueOf(msg.charAt(0)).equals("/"))
            commandMessage(msg, username);
        else{
            sendToAll(msg, username);
        }
    }
    
    public int checkName(String username) {

        for (int i = 0; i < clients.size(); i++)
            if (clients.get(i).getName().equals(username))
                return i;

        return -1;
    }
    
    public void commandMessage(String msg, String username) {

        String[] commandMSG = msg.split("\\s+");
        int pos = 0;
        switch (commandMSG[0]) {

            case "/setname":
                //check se username è disponibile
                pos = checkName(commandMSG[1]);
                if (pos == -1) {
                    //imposta username per quel client
                    pos = checkName(username);
                    clients.get(pos).setName(commandMSG[1]);
                    //invia lista client aggiornata
                    sendToAll("Ha cambiato il suo nome in " + commandMSG[1], username);
                    sendClientList();
                } else
                    sendPrivate("Username gia' in uso da un altro utente!", server, username);                   
                
                break;

            case "/tell":
                String finalMsg = "";
                for (int i = 2; i < commandMSG.length; i++) {
                    if(i < commandMSG.length - 1)
                        finalMsg += commandMSG[i] + " ";
                    else
                        finalMsg += commandMSG[i];
                }
                //invia messaggio privato
                sendPrivate(finalMsg, username, commandMSG[1]);
                System.out.println("[" + username + "] a [" + commandMSG[1] + "]:" + finalMsg);
                break;

            case "/leave":
                //rimuove il client
                removeClient(username);                
                //aggiorna e invia lista client
                sendToAll(username + " si e' disconnesso", server);
                sendClientList();
                break;

            case "/help":
                //invia lista di comandi al client
                sendHelpList(username);
                break;

            case "/userlist":
                sendPrivate("Lista degli utenti connessi:\n" + toString(), server, username);
                break;

            default:
                sendPrivate("Comando non valido!", server, username);
                
                finalMsg = "";
                for (int i = 2; i < commandMSG.length; i++) {
                    if(i < commandMSG.length - 1)
                        finalMsg += commandMSG[i] + " ";
                    else
                        finalMsg += commandMSG[i];
                }                
                System.out.println("[" + username + "]: " + finalMsg);
                break;
        }
    }
    
    public void sendToAll(String msg, String username) {

        for (ServerThread client : clients) {
            if (!client.getName().equals(username))
                if(username.equals(server))
                    client.sendMessage(GREEN + "[ALL]" + RESET + BLUE + "[" + username + "]" + RESET + ": " + msg);
                else
                    client.sendMessage(GREEN + "[ALL]" + RESET + RED + "[" + username + "]" + RESET + ": " + msg);
        }
         System.out.println("[" + username + "]: " + msg);   
    }
    
    public void sendPrivate(String msg, String sender, String receiver) {

        int pos = checkName(receiver);
        if (pos != -1)
            if(sender.equals(server))
                clients.get(pos).sendMessage(BLUE + "[" + sender + "]" + RESET +": " + msg);
            else   
                clients.get(pos).sendMessage(RED + "[" + sender + "]" + RESET +": " + msg);
        else
            sendPrivate("Utente non trovato", server, sender);

    }
    
    public String getTime() {
        //ricava l'ora corrente
        String time = "";
        time += LocalTime.now();
        
        String[] str = time.split("\\.") ;
        return "(" + str[0] + ")";
    }
    
    public void removeClient(String username) {

        int pos = checkName(username);
        if (pos != -1)
            clients.get(pos).closeConnection();
            clients.remove(pos);
    }
    
    public void sendClientList() {

        for (ServerThread client : clients)
            client.sendMessage("Lista degli utenti connessi:\n" + toString());
    }
    
    public void sendHelpList(String username) {
    
        String str = "";
        for (String cmd : helpList) {

            str += cmd + "\n";
        }
        sendPrivate(str, server, username);
    }
    
    @Override
    public String toString() {
        String clientList = "";
        for (ServerThread client : clients)
            clientList += "    --[" + client.toString() + "]\n";
            
        return clientList;
    }
}
