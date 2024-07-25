package projectserversystem;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ProjectServerSystem {

    private final ArrayList<ServerThreaded> threadList;
    private final HashMap<String, ServerThreaded> clientMap;
    private static final int SERVER_PORT = 7454;

    public ProjectServerSystem() {
        threadList = new ArrayList<>();
        clientMap = new HashMap<>();
    }

    public static void main(String[] args) {
        new ProjectServerSystem().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is running..." );
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThreaded serverThread = new ServerThreaded(socket, this, threadList, clientMap);
                threadList.add(serverThread);
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println("Error occurred in server main: " + e.getMessage());
        }
    }
}
