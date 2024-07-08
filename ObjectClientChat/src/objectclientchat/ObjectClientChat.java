package objectclientchat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ObjectClientChat {

    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private String clientName;
    private String targetClientName;
    private static final Scanner scanner = new Scanner(System.in);

    public ObjectClientChat(String serverAddress, int port) throws Exception {
        this.socket = new Socket(serverAddress, port);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }

    public void start() {
        try {
            System.out.println("Enter your name: ");
            clientName = scanner.nextLine();
            while (!ClientDirt.isValidClientName(clientName)) {
                System.out.println("Invalid name. Please enter a valid name: ");
                clientName = scanner.nextLine();
            }
            ClientDirt.createClientDirectory(clientName);
            output.println(clientName);

            ClientThreaded clientThread = new ClientThreaded(socket, input, output, clientName);
            clientThread.start();

            while (true) {
                if (targetClientName == null || targetClientName.isEmpty()) {
                    setTargetClientName();
                }

                System.out.print("(" + clientName + ") message for " + targetClientName + ": ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    output.println("exit");
                    break;
                } else if (userInput.equalsIgnoreCase("switch")) {
                    ClientThreaded.displayActiveUsers();
                    setTargetClientName();
                } else {
                    ClientDirt.logMessageToFile(clientName, targetClientName, userInput, false);
                    output.println(targetClientName + " " + userInput);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred in client: " + e.getMessage());
        }
    }

    private void setTargetClientName() {
        System.out.println("Enter the name of the client you want to chat with: ");
        targetClientName = scanner.nextLine();
        while (!ClientDirt.isValidClientName(targetClientName)) {
            System.out.println("Invalid name. Please enter a valid name: ");
            targetClientName = scanner.nextLine();
        }
        ClientDirt.createsubClientDirectory(clientName, targetClientName);
        ClientThreaded.displayChatHistory(targetClientName);
    }

    public static void main(String[] args) {
        try {
            ObjectClientChat client = new ObjectClientChat("localhost", 8001);
            client.start();
        } catch (Exception e) {
            System.out.println("Exception occurred in client main: " + e.getMessage());
        }
    }
}