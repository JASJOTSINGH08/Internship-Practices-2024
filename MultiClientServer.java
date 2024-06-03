import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiClientServer {
    private static final int PORT = 1234;
    private static volatile boolean running = true;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        // Start a thread to listen for shutdown commands
        new Thread(() -> {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while (running) {
                    String command = consoleReader.readLine();
                    if (command.equalsIgnoreCase("shutdown")) {
                        running = false;
                        System.out.println("Server is shutting down...");
                        if (serverSocket != null && !serverSocket.isClosed()) {
                            serverSocket.close();
                        }
                        threadPool.shutdown();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);

            // Main loop to accept client connections
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

                    // Handle client communication in a new thread
                    threadPool.execute(new ClientHandler(clientSocket));
                } catch (IOException e) {
                    if (!running) {
                        System.out.println("Server has been shut down.");
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            if (!running) {
                System.out.println("Server has been shut down.");
            } else {
                e.printStackTrace();
            }
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            threadPool.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);

                    if (inputLine.trim().equalsIgnoreCase("/exit")) {
                        writer.println("Server: Closing connection...");
                        break;
                    }

                    writer.println("Server: " + inputLine);
                }

                System.out.println("Connection closed");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
