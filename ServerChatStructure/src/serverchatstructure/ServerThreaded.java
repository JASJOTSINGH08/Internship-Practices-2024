package serverchatstructure;

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
    private final HashMap<String, ServerThreaded> clientMap;
    private String clientName;
    private PrintWriter output;

    public ServerThreaded(Socket socket, ArrayList<ServerThreaded> threadList, HashMap<String, ServerThreaded> clientMap) {
        this.socket = socket;
        this.clientMap = clientMap;
    }

    @Override
    public void run() {
        try {
            socket.setSoTimeout(TIMEOUT);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            clientName = input.readLine();
            synchronized (clientMap) {
                clientMap.put(clientName, this);
            }
            printActiveClients();

            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("exit")) break;
                String[] parts = inputLine.split(" ", 2);
                if (parts.length < 2) continue;

                String targetClientName = parts[0];
                String message = parts[1];
                sendToClient(targetClientName, message);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Client " + clientName + " TimeOut");
            notifyClientTimeout();
        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
        } finally {
            try {
                socket.close();
                synchronized (clientMap) {
                    clientMap.remove(clientName);
                }
                printActiveClients();
            } catch (IOException e) {
                System.out.println("Exception occurred while closing socket: " + e.getMessage());
            }
        }
    }

    private void sendToClient(String targetClientName, String message) {
        ServerThreaded targetClient;
        synchronized (clientMap) {
            targetClient = clientMap.get(targetClientName);
        }

        String report = getTimeStamp()  + " " + message;

        if (targetClient != null) {
            targetClient.output.println(clientName + " " + report);
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

    private void printActiveClients() {
        synchronized (clientMap) {
            System.out.println("Current Clients:");
            for (String client : clientMap.keySet()) {
                System.out.println("(" + client + ", online)");
            }
            System.out.println("---");
        }
    }
    
     private void notifyClientTimeout() {
        if (output != null) {
            output.println("You have been timed out due to inactivity.");
            output.flush();
        }
    }
}
