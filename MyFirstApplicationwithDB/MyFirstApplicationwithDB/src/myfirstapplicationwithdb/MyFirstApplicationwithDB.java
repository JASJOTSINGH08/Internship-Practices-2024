/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package myfirstapplicationwithdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author arun-cts
 */
public class MyFirstApplicationwithDB {
    private static String DBDriver = "com.mysql.jdbc.Driver";
     private static String DBUrl = "";
    private static String DBUser = "";
    private static String DBPass = "";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         try {
            Class.forName(DBDriver);
            MyFirstApplicationwithDB myclass = new MyFirstApplicationwithDB();
            myclass.getClientServerlist();
            
         }catch(Exception ex){
         
         }
        
    }
         public void getClientServerlist() {

        Statement st = null;
        Connection con = null;
        ResultSet rs = null;
        ArrayList<String[]> list = new ArrayList<>();
        try {
            con = DriverManager.getConnection(DBUrl, DBUser, DBPass);
            st = con.createStatement();
            String query = "Write Query";
            System.out.println("Getting All Server list " + query);
            rs = st.executeQuery(query);
            while (rs.next()) {
                String[] tempelement = new String[2];
                tempelement[0] = rs.getString("serverID");
                tempelement[1] = rs.getString("serverType");
                
                list.add(tempelement);
            }

        } catch (Exception ex) {
            System.out.println("System Error While fetching server list ..." + ex.toString());

        } finally {
            try {
                rs.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
            }
        }

        
    }
    
}
