/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myfirstapplicationwithdbhashmap;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;

public class DatabaseOperationsConn {
    private static final String DBDriver = "com.mysql.jdbc.Driver";
    private static final String DBUrl = "jdbc:mysql://localhost:7896/DatabaseConnection";  
    private static final String DBUser = "root";
    private static final String DBPass = "Ja@080405";
    
    public DatabaseOperationsConn() {
        try {
            Class.forName(DBDriver);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: Unable to load driver class!");
            ex.printStackTrace();
        }
    }
    
    public void insertData(String IMEIval, long cardIdDecimal, String dateAscii, String timeAscii) {
        try  {
            Connection con = DriverManager.getConnection(DBUrl, DBUser, DBPass);
            String insertQuery = "INSERT INTO data (IMEIval, cardIdHex, dateHex, timeHex) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, IMEIval);
            pst.setString(2, Long.toString(cardIdDecimal));
            pst.setString(3, dateAscii);
            pst.setString(4, timeAscii);
            pst.executeUpdate();
            System.out.println("Data inserted successfully!");
        } catch (SQLException ex) {
            System.out.println("System Error While inserting data: " + ex.toString());
        }
    }
    
    public HashMap<String, ArrayList<String>> fetchDataMap() {
        HashMap<String, ArrayList<String>> dataList;
        dataList = new HashMap<>();
        try (Connection con = DriverManager.getConnection(DBUrl, DBUser, DBPass)) {
            String fetchQuery = "SELECT IMEIval, cardIdHex, dateHex FROM data";
            PreparedStatement pst = con.prepareStatement(fetchQuery);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                ArrayList<String> dataListEntry = new ArrayList<>();
                dataListEntry.add(rs.getString("IMEIval"));
                dataListEntry.add(rs.getString("cardIdHex"));
                dataListEntry.add(rs.getString("dateHex"));
                
                dataList.put(dataListEntry.get(0), dataListEntry);
                
             
            }
        } catch (SQLException ex) {
            System.out.println("System Error While fetching data: " + ex.toString());
        }
        return dataList;
    }
}