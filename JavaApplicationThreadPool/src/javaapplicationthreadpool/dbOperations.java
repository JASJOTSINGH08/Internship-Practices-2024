package javaapplicationthreadpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dbOperations {
       private static final String DBDriver = "com.mysql.jdbc.Driver";
    private static final String DBUrl = "jdbc:mysql://localhost:7896/DatabaseConnection";
    private static final String DBUser = "root";
    private static final String DBPass = "Ja@080405";
    
    public dbOperations() {
        try {
            Class.forName(DBDriver);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: Unable to load driver class!");
        }
    }
    
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DBUrl, DBUser, DBPass);
    }
    
    public boolean isDatabaseConnected() {
        try (Connection connection = connect()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public boolean isUsernameUnique(String username) {
        if (!isDatabaseConnected()) {
            System.out.println("Database connection is not available. Storing locally.");
            return true; 
        }

        String query = "SELECT COUNT(*) FROM datafiles WHERE username = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking username uniqueness: " + e.getMessage());
        }
        return false;
    }
    
    public void registerUser(String username, String password) {
        if (!isDatabaseConnected()) {
            System.out.println("Database connection is not available. Storing user locally.");
            return;
        }

        String query = "INSERT INTO datafiles (username, password) VALUES (?, ?)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            System.out.println("User registered successfully!");
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }
    

}
