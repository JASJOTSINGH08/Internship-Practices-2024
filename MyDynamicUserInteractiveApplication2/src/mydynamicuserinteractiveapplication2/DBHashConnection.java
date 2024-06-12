/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mydynamicuserinteractiveapplication2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHashConnection {

    private static final String DBDriver = "com.mysql.jdbc.Driver";
    private static final String DBUrl = "jdbc:mysql://localhost:7896/DatabaseConnection";  
    private static final String DBUser = "root";
    private static final String DBPass = "Ja@080405";
    
    public DBHashConnection() {
        try {
            Class.forName(DBDriver);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: Unable to load driver class!");
        }
    }
    
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DBUrl, DBUser, DBPass);
    }
    
    public boolean isUsernameUnique(String username) {
        String query = "SELECT COUNT(*) FROM usersHashMap WHERE username = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) == 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }
    
     public boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM usersHashMap WHERE username = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }
     
     
    public void registerUser(String username, String password) {
        String query = "INSERT INTO usersHashMap (username, password) VALUES (?, ?)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            System.out.println("User registered successfully!");
        } catch (SQLException e) {
    }  
}
    
    
      public boolean authenticateUser(String username, String password) {
    String query = "SELECT * FROM  usersHashMap WHERE username= ? AND password= ?";
    try (Connection connection = connect();
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, username);
        statement.setString(2, password);
         ResultSet resultantSet = statement.executeQuery();
         return resultantSet.next();
       
    } catch (SQLException e) {
        System.out.println("Error authenticating user: " + e.getMessage());
        return false;
    }
}   
      
      
         public void printAllUsers(){
        String query = "SELECT username, password FROM usersHashMap";
        try (Connection connection = DriverManager.getConnection(DBUrl,DBUser,DBPass);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()){
            System.out.println("Stored Users: ");
            while (resultSet.next()){
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                System.out.println("Username: " + username + "  |  " + "Password: "+ password);
                System.out.println("__________________________________________________________________________________________________________");
            }
        }  catch (SQLException e) {
    }
  }   
}
