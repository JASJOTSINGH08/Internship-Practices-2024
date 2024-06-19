package javaapplicationfilethreadhandling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFOperations {
    private static final String DBDriver = "com.mysql.jdbc.Driver";
    private static final String DBUrl = "jdbc:mysql://localhost:7896/DatabaseConnection";
    private static final String DBUser = "root";
    private static final String DBPass = "Ja@080405";
    
    public DBFOperations() {
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
            // Handle scenario where database connection is not available
            System.out.println("Database connection is not available. Storing locally.");
            return true; // Assume username is unique in file storage
        }

        String query = "SELECT COUNT(*) FROM filestore WHERE username = ?";
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
    
    public boolean isUsernameExists(String username) {
        if (!isDatabaseConnected()) {
            return false; // Return false if database connection is not available
        }

        String query = "SELECT COUNT(*) FROM filestore WHERE username = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking if username exists: " + e.getMessage());
        }
        return false;
    }
    
    public void registerUser(String username, String password) {
        if (!isDatabaseConnected()) {
            // If database connection is not available, store in file directly
            System.out.println("Database connection is not available. Storing user locally.");
            // Example: usersList.add(new String[]{username, password});
            return;
        }

        String query = "INSERT INTO filestore (username, password) VALUES (?, ?)";
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
    

    public boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM filestore WHERE username = ? AND password = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
            return false;
        }
    }

    public void printAllUsers() {
        String query = "SELECT username, password FROM filestore";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            System.out.println("Stored Users: ");
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                System.out.println("Username: " + username + "  |  Password: " + password);
                System.out.println("_______________________________________");
            }
        } catch (SQLException e) {
        }
    }
}

