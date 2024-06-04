import java.io.*;
import java.net.*;

public class TelnetClient {
    private String host;

    public TelnetClient(String host) {
        this.host = host;
    }

    public void getData() {
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(host, 23)); // Connect to the Telnet server on port 23

            System.out.println("Connected to " + host);

            // Set up input and output streams
            PrintWriter s_out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader s_in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            // Send commands to the server
            s_out.println("username"); // Replace with your actual username
            this.read(s_in); // Read the response

            s_out.println("password"); // Replace with your actual password
            this.read(s_in); // Read the response

            // Close the connection
            s.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void read(BufferedReader br) throws IOException {
        char[] ca = new char[1024];
        int rc = br.read(ca);
        String s = new String(ca).trim();
        Arrays(ca, (char) 0);
        System.out.println("RC=" + rc + ": " + s);
    }

    public static void main(String[] args) {
        TelnetClient client = new TelnetClient("your_telnet_server_ip_or_hostname");
        client.getData();
    }
}
