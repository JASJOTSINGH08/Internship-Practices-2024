package jsonclientchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import org.json.simple.parser.ParseException;

public class JSONClientChat {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8012;
    private static String clientName;
    private static String targetClientName;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Enter your name: ");
            clientName = scanner.nextLine();  
            while (!ClientDirt.isValidClientName(clientName)) {
                System.out.println("Invalid name. Please enter a valid name: ");
                clientName = scanner.nextLine();
            }

            ClientDirt.createClientDirectory(clientName);
            output.println(clientName);

            ClientThreaded clientThread = new ClientThreaded(socket, input, output, clientName);
            clientThread.start();

            while (true) {
                if (targetClientName == null || targetClientName.isEmpty()) {
                    setTargetClientName();
                }

                System.out.print("(" + clientName + ")" + " message for " + targetClientName + ": ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    output.println("exit");
                    break;
                } else if (userInput.equalsIgnoreCase("switch")) {                  
                    System.out.println("Enter the name of the client you want to chat with: ");
                    targetClientName = scanner.nextLine();
                } else if (userInput.equalsIgnoreCase("search")){
                    searchMessages();
                } else {                
                    ClientDirt.logMessageToFile(clientName, targetClientName, userInput, false);
                    output.println(targetClientName + " " + userInput);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred in client: " + e.getMessage());
        }
    }

    private static void setTargetClientName() {
        System.out.println("Enter the name of the client you want to chat with: ");
        targetClientName = scanner.nextLine();

        while (!ClientDirt.isValidClientName(targetClientName)) {
            System.out.println("Invalid name. Please enter a valid name: ");
            targetClientName = scanner.nextLine();
        }
      
        ClientDirt.createsubClientDirectory(clientName, targetClientName);
        ClientThreaded.displayChatHistory(targetClientName);
    }
    
    private static void searchMessages() throws java.text.ParseException{
        System.out.println("You want to search by 'date' or 'query' : ");
        String choice = scanner.nextLine().trim().toLowerCase();
        
        Date date = null;
        String searchquery = null;
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yy HH:mm:ss") ;
                
        switch(choice){
            case "date" :
                System.out.println("Enter date you want to search (dd-MM-yy HH:mm:ss): ");
                String dateval = scanner.nextLine().trim();
                
                if (!dateval.isEmpty()){
                    date = dateformat.parse(dateval);
                }
                break;
                
            case "query":
                System.out.println("Enter search query: ");
                searchquery = scanner.nextLine().trim();
                break;
                
            default:
                System.out.println("Invalid Choice. Enter 'date' or 'query' for search: ");
                return;
        }
        ClientThreaded.searchMessages(targetClientName, searchquery, date);
    }
}
