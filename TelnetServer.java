import java.io.*;
import java.net.*;

public class TelnetServer {
    public static void main(String[] args) {
        try {
            // Create a server socket on port 23 (Telnet default port)
            ServerSocket serverSocket = new ServerSocket(23);
            System.out.println("Telnet Server started. Waiting for clients...");

            while (true) {
                // Accept incoming client connections
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);

                // Set up input and output streams
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client
                out.println("Welcome to the Telnet Server. Type 'exit' to close the connection.");

                String input;
                while ((input = in.readLine()) != null) {
                    System.out.println("Received from client: " + input);
                    out.println("Server received: " + input);

                    if (input.equalsIgnoreCase("exit")) {
                        socket.close();
                        System.out.println("Connection closed by client.");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
