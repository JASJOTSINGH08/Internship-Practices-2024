package clientchatstructure;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientChatStructure {

    static HashMap<Integer, String> activeClients = new HashMap<>();

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8001)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter your name: ");
            String clientName = scanner.nextLine();
            while (!ClientDirt.isValidClientName(clientName)) {
                System.out.println("Invalid name. Please enter a valid name: ");
                clientName = scanner.nextLine();
            }
            ClientDirt.createClientDirectory(clientName);
            output.println(clientName);

            ClientThreaded clientThread = new ClientThreaded(socket, input, output, clientName);
            clientThread.start();

            String targetClientName = "";
            while (true) {
                if (targetClientName.isEmpty()) {
                    System.out.println("Enter the name of the client you want to chat with: ");
                    targetClientName = scanner.nextLine();
                    while (!ClientDirt.isValidClientName(targetClientName)) {
                        System.out.println("Invalid name. Please enter a valid name: ");
                        targetClientName = scanner.nextLine();
                    }
                    ClientDirt.createsubClientDirectory(clientName, targetClientName);
                    ClientThreaded.displayChatHistory(targetClientName);
                }

                System.out.print("(" + clientName + ")" + " message for " + targetClientName + ": ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    output.println("exit");
                    break;
                } else if (userInput.equalsIgnoreCase("switch")) {
                    ClientThreaded.displayActiveUsers();
                    System.out.println("Enter the name of the client you want to chat with: ");
                    targetClientName = scanner.nextLine();
                    while (!ClientDirt.isValidClientName(targetClientName)) {
                        System.out.println("Invalid name. Please enter a valid name: ");
                        targetClientName = scanner.nextLine();
                    }
                    ClientDirt.createsubClientDirectory(clientName, targetClientName);
                    ClientThreaded.displayChatHistory(targetClientName);
                } else {
                    ClientDirt.logMessageToFile(clientName, targetClientName, userInput, false);
                    output.println(targetClientName + " " + userInput);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred in client main: " + e.getMessage());
        }
    }
}
