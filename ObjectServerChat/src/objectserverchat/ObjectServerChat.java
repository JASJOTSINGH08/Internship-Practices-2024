package objectserverchat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class ObjectServerChat {
 private final ArrayList<ServerThreaded> threadList;
    private final HashMap<String, ServerThreaded> clientMap;

    public ObjectServerChat() {
        threadList = new ArrayList<>();
        clientMap = new HashMap<>();
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
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

    public static void main(String[] args) {
        ObjectServerChat server = new ObjectServerChat();
        server.start(8001);
    }
}


        

