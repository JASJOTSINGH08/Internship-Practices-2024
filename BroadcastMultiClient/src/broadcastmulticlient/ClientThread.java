package broadcastmulticlient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private final BufferedReader input;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String response = input.readLine();
                if (response == null) {
                    break;
                }
                System.out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Exception occurred in ClientThread: " + e.getMessage());
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                System.out.println("Exception occurred while closing input stream: " + e.getMessage());
            }
        }
    }
}
