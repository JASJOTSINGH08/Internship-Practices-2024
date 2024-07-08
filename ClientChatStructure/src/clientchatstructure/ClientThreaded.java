package clientchatstructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ClientThreaded extends Thread {
    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private static String clientName;
    private static final HashMap<Integer, String> activeClients = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    public ClientThreaded(Socket socket, BufferedReader input, PrintWriter output, String clientName) {
        this.socket = socket;
        this.input = input;
        this.output = output;
        ClientThreaded.clientName = clientName;
    }

    @Override
    public void run() {
        try {
            String response;
            while ((response = input.readLine()) != null) {
                if (response.startsWith("ACTIVE_USERS")) {
                    updateActiveUsers(response);
                } else if (response.contains("Server received from")) {
                    System.out.println(">> " + response);
                } else {
                    System.out.println("<< " + response);
                    String[] parts = response.split(" ", 2);
                    if (parts.length >= 2) {
                        String sender = parts[0];
                        String message = parts[1];
                     //   ClientDirt.logMessageToFile(sender, clientName, message, true);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Exception occurred in ClientThread: " + e.getMessage());
        }
    }

    private void updateActiveUsers(String response) {
        activeClients.clear();
        String[] users = response.substring("ACTIVE_USERS".length()).split(",");
        for (int i = 0; i < users.length; i++) {
            activeClients.put(i + 1, users[i].trim());
        }
        displayActiveUsers();
    }

    public static void displayActiveUsers() {
       // System.out.println("List of Active Clients:");
        for (Map.Entry<Integer, String> entry : activeClients.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
    }

    public static void displayChatHistory(String targetClientName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(ClientDirt.CHAT_LOG_DIR + "/" + clientName + "/" + targetClientName + ".txt"));
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading chat history: " + e.getMessage());
        }
    }
}
