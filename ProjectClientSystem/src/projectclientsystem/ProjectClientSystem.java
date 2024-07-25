package projectclientsystem;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ProjectClientSystem {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 7454;
    private static String clientName;
    private static PrintWriter output;
    private static BufferedReader input;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI().createAndShowGUI());
    }

    public static void startClient(String name, JTextArea chatArea) {
        clientName = name;
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            output.println(clientName);

            ClientThreaded clientThread = new ClientThreaded(socket, input, output, clientName, chatArea);
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage());
        }
    }

    public static PrintWriter getOutput() {
        return output;
    }
}
