package jsonclientchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientThreaded extends Thread {
    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private static String clientName;

    public ClientThreaded(Socket socket, BufferedReader input, PrintWriter output, String clientName) {
        this.socket = socket;
        this.input = input;
        this.output = output;
        ClientThreaded.clientName = clientName;
    }

    @Override
    public void run() {
        try {
            String response;
            while ((response = input.readLine()) != null) {
                if (response.contains("Server received from")) {
                    System.out.println(">> " + response);
                } else {
                    System.out.println("<< " + response);
                }
            }
        } catch (IOException e) {
            System.out.println("Exception occurred in ClientThread: " + e.getMessage());
        }
    }

    public static void displayChatHistory(String targetClientName) {
        JSONParser parser = new JSONParser();
        JSONArray messages;
        try {
            String filePath = ClientDirt.CHAT_LOG_DIR + "/" + clientName + "/" + targetClientName + ".json";
            if (Files.exists(Paths.get(filePath))) {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                messages = (JSONArray) parser.parse(content);

                for (Object obj : messages) {
                    JSONObject message = (JSONObject) obj;
                    System.out.println(message.get("date-time") + " [" + message.get("type") + "] (" + message.get("name") + "): " + message.get("message"));
                }
            } else {
                System.out.println("No chat history found.");
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading chat history: " + e.getMessage());
        }
    }
    
    public static void searchMessages(String targetClientName, String searchQuery, Date date) throws java.text.ParseException {
        JSONParser parser = new JSONParser();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

        try {
            String filePath = ClientDirt.CHAT_LOG_DIR + "/" + clientName + "/" + targetClientName + ".json";
            if (Files.exists(Paths.get(filePath))) {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                JSONArray messages = (JSONArray) parser.parse(content);
                boolean foundMessages = false;

                for (Object obj : messages) {
                    JSONObject message = (JSONObject) obj;
                    String messageText = (String) message.get("message");
                    Date messageDate = dateFormat.parse((String) message.get("date-time"));

                    boolean matchesSearchQuery = (searchQuery == null || searchQuery.isEmpty() || messageText.contains(searchQuery));
                    boolean matchesDate = (date == null || messageDate.equals(date));

                    if (matchesSearchQuery && matchesDate) {
                        System.out.println(message.get("date-time") + " [" + message.get("type") + "] (" + message.get("name") + "): " + message.get("message"));
                        foundMessages = true;
                    }
                }
                if (!foundMessages) {
                    System.out.println("No messages found for the given search criteria.");
                }
            } else {
                System.out.println("Chat history not found!");
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error searching chat history: " + e.getMessage());
        }
    }
}
