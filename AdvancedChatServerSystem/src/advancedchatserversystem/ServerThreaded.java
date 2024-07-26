package advancedchatserversystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ServerThreaded extends Thread {

    private static final int TIMEOUT = 220000; 
    private final Socket socket;
    private final AdvancedChatServerSystem server;
    private final HashMap<String, ServerThreaded> clientMap;
    private String clientName;
    private PrintWriter output;
    private BufferedReader input;

    public ServerThreaded(Socket socket, AdvancedChatServerSystem server, ArrayList<ServerThreaded> threadList, HashMap<String, ServerThreaded> clientMap) {
        this.socket = socket;
        this.server = server;
        this.clientMap = clientMap;
        try {
            this.socket.setSoTimeout(TIMEOUT);
        } catch (IOException e) {
            System.out.println("Error setting socket timeout: " + e.getMessage());
        }
    }

    @Override
    public void run() {
    try {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);

        clientName = input.readLine();
        
         synchronized (clientMap) {
            if (clientMap.containsKey(clientName)) {
                output.println("Error! -> Name Already Online...");
                output.flush();
                return;
            } else {
                clientMap.put(clientName, this);
            }
        }

        printActiveClients();

        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if (inputLine.startsWith("message:")) {
                String[] parts = inputLine.split(":", 3);
                String targetClientName = parts[1];
                String message = parts[2];

                sendToClient(targetClientName, message);
            }
        }
    } catch (SocketTimeoutException e) {
        System.out.println("Client '" + clientName + "' timed out.");
        notifyClientTimeout();
    } catch (IOException e) {
        System.out.println("Error handling client " + clientName + ": " + e.getMessage());
    } finally {
        try {
            socket.close();
            synchronized (clientMap) {
                clientMap.remove(clientName);
            }
        } catch (IOException e) {
            System.out.println("Error closing socket for client " + clientName + ": " + e.getMessage());
        }
    }
}


     public void sendToClient(String targetClientName, String message) {
        ServerThreaded targetClient;
        synchronized (clientMap) {
            targetClient = clientMap.get(targetClientName);
        }

        String report = getTimeStamp() + " " + message;

        if (targetClient != null) {
            targetClient.output.println("<< " + clientName +" to "+ targetClientName + " " + report);
            targetClient.output.flush();
        } else {
            String errorMessage = getTimeStamp() + " Client " + targetClientName + " not found.";
            output.println(errorMessage);
            System.out.println("Failed to send message to " + targetClientName + " from " + clientName + ": client not active");
        }
    }


    private String getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private void notifyClientTimeout() {
        if (output != null) {
            output.println("You have been timed out due to inactivity.");
            output.flush();
        }
    }

    private void printActiveClients() {
        synchronized (clientMap) {
            System.out.println("Current Clients:");
            for (String client : clientMap.keySet()) {
                System.out.println("(" + client + ", online)");
            }
            System.out.println("---");
        }
    }
}

