package clientchatstructure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientDirt {

    public static final String CHAT_LOG_DIR = "chat_logs";

    public static boolean isValidClientName(String name) {
        return name.matches("[a-zA-Z ]+");
    }

    public static void createClientDirectory(String clientName) {
        File clientDir = new File(CHAT_LOG_DIR, clientName);
        if (!clientDir.exists()) {
            clientDir.mkdirs();
        }
    }
    
    public static void createsubClientDirectory(String clientName, String targetClientName) {
        createClientDirectory(clientName);
        File subFile = new File(CHAT_LOG_DIR + "/" + clientName, targetClientName + ".txt");
        try {
            if (!subFile.exists()) {
                subFile.createNewFile();
            } else {
                System.out.println("File already exists: " + subFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    public static void logMessageToFile(String sender, String receiver, String message, boolean isReceiver) {
         if (message.equalsIgnoreCase("switch") || message.equalsIgnoreCase("exit")) {
            return; 
        }
         
        try {
            createClientDirectory(sender);
            createClientDirectory(receiver);

            File senderFile = new File(CHAT_LOG_DIR + "/" + sender, receiver + ".txt");
            try (PrintWriter senderLog = new PrintWriter(new FileWriter(senderFile, true))) {
                senderLog.println((isReceiver ? "<<" : ">>") + " " + getTimeStamp() + " " + " ( " + sender + " ) " +  message);
            }

            File receiverFile = new File(CHAT_LOG_DIR + "/" + receiver, sender + ".txt");
            try (PrintWriter receiverLog = new PrintWriter(new FileWriter(receiverFile, true))) {
                receiverLog.println((isReceiver ? ">>" : "<<") +  " " + getTimeStamp() + " " + " ( " + sender + " ) " +  message);
            }

        } catch (IOException e) {
            System.out.println("Error logging message: " + e.getMessage());
        }
    }
    
     private static String getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
