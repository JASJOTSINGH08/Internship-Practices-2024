package projectclientsystem;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientDirt {

    public static  String CHAT_LOG_DIR = "chat_logs";
    

    public static void createClientDirectory(String clientName) {
        File clientDir = new File(CHAT_LOG_DIR, clientName);
        if (!clientDir.exists()) {
            clientDir.mkdirs();
        }
    }
    
    public static void createsubClientDirectory(String clientName, String targetClientName) {
        createClientDirectory(clientName);
        File subFile = new File(CHAT_LOG_DIR + "/" + clientName, targetClientName + ".json");
        try {
            if (!subFile.exists()) {
                subFile.createNewFile();
                JSONArray jsonArray = new JSONArray();
                try (FileWriter fileWriter = new FileWriter(subFile)) {
                    fileWriter.write(jsonArray.toJSONString());
                    fileWriter.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Error creating subdirectory: " + e.getMessage());
        }
    }
    
    public static void logMessageToFile(String clientName, String targetClientName, String message, boolean isSent) {
        //createClientDirectory(clientName);
        //createsubClientDirectory(clientName, targetClientName);
        
        String dateTime = new SimpleDateFormat("dd-MM-yy HH:mm:ss").format(new Date());
        String messageType = isSent ? "sent" : "received";
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date-time", dateTime);
        if (isSent){
        jsonObject.put("name", clientName);
        }
        else {
        jsonObject.put("name", targetClientName);
        }
        jsonObject.put("message", message);
        jsonObject.put("type", messageType);
        
        try {
            JSONParser parser = new JSONParser();
            String filePath = CHAT_LOG_DIR + "/" + clientName + "/" + targetClientName + ".json";
            File file = new File(filePath);
            JSONArray messages;
            
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                messages = (JSONArray) parser.parse(content);
            } else {
                messages = new JSONArray();
            }
            
            messages.add(jsonObject);
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(messages.toJSONString());
                fileWriter.flush();
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error logging message: " + e.getMessage());
        }
    }

}
