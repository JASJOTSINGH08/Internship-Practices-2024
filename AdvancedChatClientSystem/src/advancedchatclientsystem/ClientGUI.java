package advancedchatclientsystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class ClientGUI {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 7454;
    private static final String CLIENTS_DATABASE = "clients_database.txt";
    
    private JFrame frame;
    private JPanel panel;
    private JLabel welcomeLabel;
    private JTextField nameField;
    private JTextField chatWithField;
    JTextPane chatPane;
    private JTextField messageField;
    private JButton loginButton;
    private PrintWriter output;
    private String clientName;
    private String targetClientName;
    private BufferedReader input;

    
    public void createAndShowGUI() {
        frame = new JFrame("Corporate Chat System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        
        panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        
        frame.setVisible(true);
    }
    
    private void placeComponents(JPanel panel) {
        panel.setLayout(null);
        
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setBounds(10, 20, 160, 25);
        panel.add(nameLabel);
        
        nameField = new JTextField(20);
        nameField.setBounds(180, 20, 160, 25);
        panel.add(nameField);
        
        loginButton = new JButton("Login");
        loginButton.setBounds(180, 50, 160, 25);
        panel.add(loginButton);
        
        chatPane = new JTextPane();
        chatPane.setEditable(false);
    
        JScrollPane scrollPane = new JScrollPane(chatPane);
        scrollPane.setBounds(10, 90, 360, 200);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane);

        
        messageField = new JTextField(20);
        messageField.setBounds(10, 300, 260, 25);
        panel.add(messageField);
        
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(280, 300, 90, 25);
        panel.add(sendButton);
        
        loginButton.addActionListener(e -> handleLogin());
        sendButton.addActionListener(e -> handleSendMessage());
        
        nameField.addActionListener(e -> handleLogin());
    }
    
    private void handleLogin() {
        String name = nameField.getText();
        if (isValidClientName(name)) {
            if (!isNameInDatabase(name)) {
                int option = JOptionPane.showConfirmDialog(panel,
                        "Name '" + name + "' is not registered. Do you want to add it?",
                        "Name Not Registered",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    registerClient(name);
                } else {
                    return;
                }
            }
            clientName = name;
            startClient(clientName);
            showWelcomeScreen();
        } else {
            JOptionPane.showMessageDialog(panel, "Invalid name format. Please enter a valid name.");
        }
    }

    private void handleSendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            sendMessage(message);
            messageField.setText("");
        }
    }

    private void showWelcomeScreen() {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        
        welcomeLabel = new JLabel("Welcome, " + clientName + "!");
        welcomeLabel.setBounds(10, 20, 300, 25);
        panel.add(welcomeLabel);
        
        JLabel chatWithLabel = new JLabel("Enter name of the client to chat with:");
        chatWithLabel.setBounds(10, 50, 300, 25);
        panel.add(chatWithLabel);
        
        chatWithField = new JTextField(20);
        chatWithField.setBounds(10, 80, 160, 25);
        panel.add(chatWithField);
        
        JButton startChatButton = new JButton("Start Chat");
        startChatButton.setBounds(180, 80, 100, 25);
        panel.add(startChatButton);
        
        startChatButton.addActionListener((ActionEvent e) -> {
            String targetName = chatWithField.getText();
            if (isValidClientName(targetName)) {
                targetClientName = targetName;
                ClientDirt.createClientDirectory(clientName);
                ClientDirt.createsubClientDirectory(clientName, targetClientName);
                showChatWindow(targetClientName);
            } else {
                JOptionPane.showMessageDialog(panel, "Invalid name. Please enter a valid name.");
            }
        });      
        
          chatWithField.addActionListener((ActionEvent e) -> {
            String targetName = chatWithField.getText();
            if (isValidClientName(targetName)) {
                targetClientName = targetName;
                ClientDirt.createClientDirectory(clientName);
                ClientDirt.createsubClientDirectory(clientName, targetClientName);
                showChatWindow(targetClientName);
            } else {
                JOptionPane.showMessageDialog(panel, "Invalid name. Please enter a valid name.");
            }
        });
        
        frame.setVisible(true);
    }
    
    private void showChatWindow(String targetClientName) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        
        JLabel chatWithNameLabel = new JLabel(clientName + " Chatting with: " + targetClientName);
        chatWithNameLabel.setBounds(10, 10, 200, 25);
        panel.add(chatWithNameLabel);
        
        chatPane = new JTextPane();
        chatPane.setEditable (false);
        JScrollPane scrollPane = new JScrollPane(chatPane);
        scrollPane.setBounds(10, 40, 360, 200);
        panel.add(scrollPane);
        
        messageField.setText("");
        panel.add(messageField);
        
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(280, 300, 90, 25);
        panel.add(sendButton);
        
        sendButton.addActionListener((ActionEvent e) -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageField.setText("");
                
                
            }
        });
        
          messageField.addActionListener((ActionEvent e) -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageField.setText("");
            }
        });
        
        ClientThreaded.displayChatHistory(clientName, targetClientName, chatPane);
        
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(10, 330, 90, 25);
        panel.add(searchButton);     
        
          searchButton.addActionListener((ActionEvent e) -> {
        String[] options = {"Search by Query", "Search by Date"};
        int choice = JOptionPane.showOptionDialog(panel, "Select search type:", "Search Messages", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
        
        if (choice == 0) { // Search by query
            String searchQuery = JOptionPane.showInputDialog(panel, "Enter search query:");
            if (searchQuery != null) {
                try {
                    searchMessagesByQuery(clientName, targetClientName, searchQuery);
                } catch (ParseException ex) {
                    Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (choice == 1) { // Search by date
            String dateString = JOptionPane.showInputDialog(panel, "Enter date (dd-MM-yy):");
            try {
                if (dateString != null && !dateString.isEmpty()) {
                    Date date = new SimpleDateFormat("dd-MM-yy").parse(dateString);
                    searchMessagesByDate(clientName, targetClientName, date);
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid date format. Please enter date in dd-MM-yy format.");
            }
        }
    });
      
        JButton switchButton = new JButton("Switch");
        switchButton.setBounds(110, 330, 90, 25);
        panel.add(switchButton);
        
        switchButton.addActionListener((ActionEvent e) -> showWelcomeScreen());
        
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(210, 330, 90, 25);
        panel.add(exitButton);
        
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));
        
        frame.setVisible(true); 
    }

    private void startClient(String name) {
        clientName = name;
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            output.println(clientName);

            ClientThreaded clientThread = new ClientThreaded(socket, input, output, clientName, chatPane);
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        appendToChatPane(">> " + message  , true, chatPane);
        String sender = clientName;
        String receiver = targetClientName;
        ClientDirt.logMessageToFile(sender, receiver, message, true);
        output.println("message:" + receiver + ":" + message);
    }
    
    public boolean isValidClientName(String name) {
        return name.matches("[a-zA-Z ]+");
    }
    
    public void registerClient(String clientName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLIENTS_DATABASE, true))) {
            writer.write(clientName);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error registering client: " + e.getMessage());
        }
    }
    
    public boolean isNameInDatabase(String clientName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CLIENTS_DATABASE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase(clientName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading client database: " + e.getMessage());
        }
        return false;
    }
    

    public void searchMessagesByQuery(String clientName, String targetClientName, String searchQuery) throws ParseException {
    JSONParser parser = new JSONParser();
    try {
        String filePath = ClientDirt.CHAT_LOG_DIR + "/" + clientName + "/" + targetClientName + ".json";
        if (Files.exists(Paths.get(filePath))) {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray messages = (JSONArray) parser.parse(content);
            StringBuilder result = new StringBuilder();

            for (Object obj : messages) {
                JSONObject message = (JSONObject) obj;
                String messageText = (String) message.get("message");

                if (messageText.toLowerCase().contains(searchQuery.toLowerCase())) {
                    result.append(message.get("date-time")).append(" [")
                          .append(message.get("type")).append("] (")
                          .append(message.get("name")).append("): ")
                          .append(messageText).append("\n");
                }
            }

            String resultMessage = result.length() > 0 ? result.toString() : "No messages found for the query.";
            JOptionPane.showMessageDialog(panel, resultMessage, "Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panel, "Chat history not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (IOException | org.json.simple.parser.ParseException e) {
        JOptionPane.showMessageDialog(panel, "Error searching chat history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
       
    
    public void searchMessagesByDate(String clientName, String targetClientName, Date date) {
    JSONParser parser = new JSONParser();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
    String searchDate = dateFormat.format(date);

    try {
        String filePath = ClientDirt.CHAT_LOG_DIR + "/" + clientName + "/" + targetClientName + ".json";
        if (Files.exists(Paths.get(filePath))) {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray messages = (JSONArray) parser.parse(content);
            StringBuilder result = new StringBuilder();

            for (Object obj : messages) {
                JSONObject message = (JSONObject) obj;
                String timestamp = (String) message.get("date-time");

                if (timestamp.contains(searchDate)) {
                    result.append(timestamp).append(" [")
                          .append(message.get("type")).append("] (")
                          .append(message.get("name")).append("): ")
                          .append(message.get("message")).append("\n");
                }
            }

            String resultMessage = result.length() > 0 ? result.toString() : "No messages found for the date.";
            JOptionPane.showMessageDialog(panel, resultMessage, "Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panel, "Chat history not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (IOException | org.json.simple.parser.ParseException e) {
        JOptionPane.showMessageDialog(panel, "Error searching chat history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    
    private static void appendToChatPane(String message, boolean isSent, JTextPane chatPane) {
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



