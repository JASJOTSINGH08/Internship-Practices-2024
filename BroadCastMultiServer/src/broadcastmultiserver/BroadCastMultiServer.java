package broadcastmultiserver;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class BroadCastMultiServer {

    public static void main(String[] args) {
        ArrayList<ServerThread> threadList = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(5000)) { 
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, threadList);
                threadList.add(serverThread);
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println("Error occurred in main: " + e.getMessage());
        }
    }
}
