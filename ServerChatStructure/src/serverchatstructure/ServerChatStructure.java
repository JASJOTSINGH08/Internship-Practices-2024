package serverchatstructure;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerChatStructure {

    public static void main(String[] args) {
        ArrayList<ServerThreaded> threadList = new ArrayList<>();
        HashMap<String, ServerThreaded> clientMap = new HashMap<>();
        try (ServerSocket serverSocket = new ServerSocket(8001)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThreaded serverThread = new ServerThreaded(socket, threadList, clientMap);
                threadList.add(serverThread);
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println("Error occurred in main: " + e.getMessage());
        }
    }
}
