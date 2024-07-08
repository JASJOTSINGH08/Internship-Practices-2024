package broadcastmultiserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread { 
    private final Socket socket;
    private final ArrayList<ServerThread> threadList; 
    private PrintWriter output;

    public ServerThread(Socket socket, ArrayList<ServerThread> threadList) {
        this.socket = socket;
        this.threadList = threadList;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String outputString = input.readLine();
                if (outputString == null || outputString.equalsIgnoreCase("exit")) {
                    break;
                }
                printAllClients(outputString);
                System.out.println("Server received: " + outputString);
            }
        } catch (IOException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private void printAllClients(String outputString) {
        for (ServerThread st : threadList) {
            if (st != this){
            st.output.println(outputString);
        }
    }
  }
}
