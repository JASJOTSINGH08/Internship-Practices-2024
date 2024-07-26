package advancedchatclientsystem;

import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.PrintWriter; 
import java.net.Socket; 
import java.nio.file.Files; 
import java.nio.file.Paths; 
import javax.swing.JTextPane; 
import javax.swing.SwingUtilities; 
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser; 
import org.json.simple.parser.ParseException;
import static advancedchatclientsystem.ClientDirt.CHAT_LOG_DIR; 
import java.awt.Color; 
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException; 
import javax.swing.text.SimpleAttributeSet; 
import javax.swing.text.StyleConstants; 
import javax.swing.text.StyledDocument;

public class ClientThreaded extends Thread { 
    private final Socket socket; 
    private final BufferedReader input; 
    private final PrintWriter output; 
    private final String clientName; 
    private final JTextPane chatPane;

    public ClientThreaded(Socket socket, BufferedReader input, PrintWriter output, String clientName, JTextPane chatPane) {
        this.socket = socket;
        this.input = input;
        this.output = output;
        this.clientName = clientName;
        this.chatPane = chatPane;
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
                if (response.startsWith("<< ")) {
                    String[] parts = response.split(" ", 7);
                    if (parts.length >= 7) {
                        String giver = parts[1];                      
                        String getter = parts[3];                  
                        String message = parts[6];

                        ClientDirt.logMessageToFile(getter, giver, message, false);

                        String formattedMessage = "<< " + giver + ": " + message + "\n";                     
                      SwingUtilities.invokeLater(() -> appendDisplayMssgToChatPane(formattedMessage, false, chatPane));
                       
                        System.out.println(formattedMessage);
                    }
                } 
            }
        } catch (IOException e) {
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

    public static void displayChatHistory(String clientName, String targetClientName, JTextPane chatPane) {
        JSONParser parser = new JSONParser();
        JSONArray messages;
        try {
            String filePath = CHAT_LOG_DIR + "/" + clientName + "/" + targetClientName + ".json";
            if (Files.exists(Paths.get(filePath))) {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                messages = (JSONArray) parser.parse(content);

                SwingUtilities.invokeLater(() -> {
                    chatPane.setText("");
                    for (Object obj : messages) {
                        JSONObject message = (JSONObject) obj;
                        String timestamp = (String) message.get("date-time");
                        String type = (String) message.get("type");
                        String name = (String) message.get("name");
                        String msgContent = (String) message.get("message");
                        
                        boolean isSent = name.equals(clientName);
                        appendDisplayMssgToChatPane(timestamp + " [" + type + "] (" + name + "): " + msgContent + "\n", isSent, chatPane);
                    }
                });
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
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
    
    
    
     
     
    

    private static void appendDisplayMssgToChatPane(String message, boolean isSent, JTextPane chatPane) {
        StyledDocument doc = chatPane.getStyledDocument();
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();

        if (isSent) {
            StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_RIGHT);
            StyleConstants.setForeground(attributeSet, Color.BLUE);
        } else {
            StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_LEFT);
            StyleConstants.setForeground(attributeSet, Color.BLACK);
        }

        try {
            int length = doc.getLength();
            doc.insertString(length, message, attributeSet);
            doc.setParagraphAttributes(length, message.length(), attributeSet, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
}
