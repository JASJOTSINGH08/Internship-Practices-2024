package javadeviceapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBDeviceConnection {
    
    private static final String DBDriver = "com.mysql.jdbc.Driver";
    private static final String DBUrl = "jdbc:mysql://localhost:7896/DatabaseConnection";
    private static final String DBUser = "root";
    private static final String DBPass = "Ja@080405";

    public DBDeviceConnection() {
        try {
            Class.forName(DBDriver);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: Unable to load driver class!");
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(DBUrl, DBUser, DBPass);
        } catch (SQLException ex) {
            return null;
        }
    }

    public List<Map<String, Object>> getDevices() {
        List<Map<String, Object>> devices = new ArrayList<>();
        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery("SELECT * FROM Device")) {

            while (rs.next()) {
                Map<String, Object> device = new HashMap<>();
                device.put("id", rs.getInt("id"));
                device.put("IMEIval", rs.getString("IMEIval"));
                devices.add(device);
            }
        } catch (Exception ex) {
        }
        return devices;
    }

    public List<Map<String, Object>> getDeviceSettings(String imeiVal) {
        List<Map<String, Object>> settings = new ArrayList<>();
        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery("SELECT * FROM DeviceSetting WHERE IMEIval = '" + imeiVal + "'")) {

            while (rs.next()) {
                Map<String, Object> setting = new HashMap<>();
                setting.put("Devicetypes", rs.getString("Devicetypes"));
                settings.add(setting);
            }
        } catch (Exception ex) {
        }
        return settings;
    }

    public Map<String, Object> getDeviceRole(String imeiVal) {
        Map<String, Object> role = new HashMap<>();
        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery("SELECT * FROM DeviceRole WHERE IMEIval = '" + imeiVal + "'")) {

            if (rs.next()) {
                role.put("Devicerlo", rs.getString("Devicerlo"));
            }
        } catch (Exception ex) {
        }
        return role;
    }
}

