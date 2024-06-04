import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String data = in.readLine();
                System.out.println("Received data from client: " + data);

                out.println("Data received by server: " + data);

                System.out.println("Do you want to close the connection? (Y/N)");
                String choice = in.readLine();

                if (choice.equalsIgnoreCase("Y")) {
                    socket.close();
                    System.out.println("Connection closed by client.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
