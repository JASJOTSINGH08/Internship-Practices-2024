import java.io.*;
import java.net.*;

public class SingleClientServer {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            // Wait for a client to connect
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

            // Set up input and output streams
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                // Process input from client
                System.out.println("Received from client: " + inputLine);

                // Check if the received message is the exit command
                if (inputLine.trim().equalsIgnoreCase("/exit")) {
                    writer.println("Server: Closing connection..."); // Send a confirmation message to the client
                    break; // Exit the loop to close the connection with the client
                }

                // Echo back to client
                writer.println("Server: " + inputLine);
            }

            // Close streams and socket
            reader.close();
            writer.close();
            clientSocket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
