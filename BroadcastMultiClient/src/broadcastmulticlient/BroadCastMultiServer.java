/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package broadcastmulticlient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class BroadCastMultiServer {
    public static void main ( String[] args){
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            InetAddress group = InetAddress.getByName("230.0.0.0");
            int port = 7896;
            
            for (int i=0; i< 5; i++){
                String message = "Quote" + (i+1) + "Hello! Multicast";
                byte[] buf = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buf , buf.length , group , port );
                socket.send(packet);
                System.out.println("Sent:" + message);
                
                Thread.sleep(2000);
            }
        } catch (Exception e)
        {
             e.printStackTrace();
        } finally {
            if (socket!= null && !socket.isClosed()){
            socket.close();
            
        }
      }
    }
}