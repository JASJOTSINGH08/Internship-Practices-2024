package javaapplicationthreadpool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class fileHandler {
    private static final String FILE_NAME = "JavaDBFStore.txt";

    public void storeUserData(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + "," + password);
            writer.newLine();
            System.out.println("Data stored in file successfully!");
        } catch (IOException e) {
            System.out.println("Error storing data in file: " + e.getMessage());
        }
    }

    public List<String[]> readUserData() {
        List<String[]> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                users.add(userData);
            }
        } catch (IOException e) {
            System.out.println("Error reading data from file: " + e.getMessage());
        }
        return users;
    }

    public void clearUserData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.print("");
        } catch (IOException e) {
            System.out.println("Error clearing data in file: " + e.getMessage());
        }
    }

    public void uploadFileDataToDB(dbOperations dbconn) {
        List<String[]> users = readUserData();
        for (String[] user : users) {
            String username = user[0];
            String password = user[1];
            dbconn.registerUser(username, password);
        }
        clearUserData();
    } 
}
