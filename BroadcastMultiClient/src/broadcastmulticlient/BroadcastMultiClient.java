package broadcastmulticlient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BroadcastMultiClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            String userInput;
            String clientName = "empty";
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();
            
            do {
                if (clientName.equals("empty")) {
                    System.out.println("Enter your name: ");
                    userInput = scanner.nextLine();
                    clientName = userInput;
                    output.println(userInput);
                    if (userInput.equals("exit")) {
                        break;
                    }
                } else {
                    System.out.println("(" + clientName + ") message: ");
                    userInput = scanner.nextLine();
                    output.println("(" + clientName + ") message: " + userInput);
                    if (userInput.equals("exit")) {
                        break;
                    }
                }
            } while (!userInput.equals("exit"));
        } catch (Exception e) {
            System.out.println("Exception occurred in client main: " + e.getMessage());
        }
    }
}
