import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter data to send to server: ");
            String data = userInput.readLine();
            out.println(data);

            String response = in.readLine();
            System.out.println("Server response: " + response);

            System.out.print("Do you want to close the connection? (Y/N): ");
            String choice = userInput.readLine();
            out.println(choice);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
