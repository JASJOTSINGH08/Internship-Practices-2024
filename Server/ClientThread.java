
// Save this as Server/ClientThread.java
import java.io.*;
import java.net.*;

public class ClientThread implements Runnable {
    private Socket socket;
    private ServerD server;
    private String clientName;

    public ClientThread(ServerD server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("HI FROM SERVER");
            while (!socket.isClosed()) {
                try {
                    if (in.available() > 0) {
                        String input = in.readUTF();
                        for (ClientThread thatClient : server.getClients()) {
                            DataOutputStream outputParticularClient = new DataOutputStream(
                                    thatClient.getSocket().getOutputStream());
                            outputParticularClient.writeUTF(input + " GOT FROM SERVER");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
