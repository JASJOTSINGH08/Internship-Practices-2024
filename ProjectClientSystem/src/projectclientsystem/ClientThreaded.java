package projectclientsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static projectclientsystem.ClientDirt.CHAT_LOG_DIR;

public class ClientThreaded extends Thread {
    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private final String clientName;
    private final JTextArea chatArea;

    public ClientThreaded(Socket socket, BufferedReader input, PrintWriter output, String clientName, JTextArea chatArea) {
        this.socket = socket;
        this.input = input;
        this.output = output;
        this.clientName = clientName;
        this.chatArea = chatArea;
    }

    @Override
    public void run() {
        try {
            String response;
            while ((response = input.readLine()) != null) {
                if (response.equals("You have been timed out due to inactivity.")){
                    showTimeoutPrompt();
                    break;
                }                         
            
                if (response.equals("Error! -> Name Already Online...")){      
                    showErrorPrompt();
                    break;
                }                                        
                {
                if (response.startsWith("<< ")) {
                    String[] parts = response.split(" ", 7);
                    if (parts.length >= 7) {
                        String giver = parts[1];                      
                        String getter = parts[3];                  
                        String message = parts[6];

                        ClientDirt.logMessageToFile(getter , giver , message, false);

                        SwingUtilities.invokeLater(() -> chatArea.append("<< " + giver + ": " + message + "\n"));
                    }
                } 
            }
        } 
    }       catch (IOException e) {
            System.out.println("Error reading message: " + e.getMessage());
        } finally {
            try {
                socket.close();
                input.close();
                output.close();
            } catch (IOException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static void displayChatHistory(String clientName, String targetClientName, JTextArea chatArea) {
        JSONParser parser = new JSONParser();
        JSONArray messages;
        try {
            String filePath = CHAT_LOG_DIR + "/" + clientName + "/" + targetClientName + ".json";
            if (Files.exists(Paths.get(filePath))) {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                messages = (JSONArray) parser.parse(content);

                SwingUtilities.invokeLater(() -> {
                    chatArea.setText("");
                    for (Object obj : messages) {
                        JSONObject message = (JSONObject) obj;
                        String timestamp = (String) message.get("date-time");
                        String type = (String) message.get("type");
                        String name = (String) message.get("name");
                        String msgContent = (String) message.get("message");

                        chatArea.append(timestamp + " [" + type + "] (" + name + "): " + msgContent + "\n");
                    }
                });
            } else {
                SwingUtilities.invokeLater(() -> chatArea.append("No chat history found.\n"));
            }
        } catch (IOException | ParseException e) {
            SwingUtilities.invokeLater(() -> chatArea.append("Error reading chat history: " + e.getMessage() + "\n"));
        }
    }
    
    private void showTimeoutPrompt(){
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "You have been timed out due to inactivity.", "TimeOut" , JOptionPane.WARNING_MESSAGE));
        
        closeConnection();
    }
    
    private void closeConnection(){
        try{
            input.close();
            output.close();
            socket.close();
        } catch (IOException e){
            System.out.println("Error closing resources" + e.getMessage());
        }
    }
  
     private void showErrorPrompt(){
        SwingUtilities.invokeLater(() -> { JOptionPane.showMessageDialog(null, "Error! -> Name Already Online... Try Activating Again!!", "Error" , JOptionPane.WARNING_MESSAGE );
       
        System.exit(0);
        });
               
        closeConnection();
   
    }
}


