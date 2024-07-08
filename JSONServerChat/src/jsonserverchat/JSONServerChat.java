package jsonserverchat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;        
        
public class JSONServerChat {
 
    private final ArrayList<ServerThreaded> threadList;
    private final HashMap<String, ServerThreaded> clientMap;
    private static final int SERVER_PORT = 8012;

    public JSONServerChat() {
        threadList = new ArrayList<>();
        clientMap = new HashMap<>();
    }

      public static void main(String[] args) {
        new JSONServerChat().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThreaded serverThread = new ServerThreaded(socket, threadList, clientMap);
                threadList.add(serverThread);
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println("Error occurred in server main: " + e.getMessage());
        }
    }
 
}