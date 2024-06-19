package javaapplicationthreadpool;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JavaApplicationThreadPool {

    private static final int MAX_THREADS = 6;
    private static final long THREAD_SLEEP_TIME = 20000; 
    private static final fileHandler fileHandler = new fileHandler();
    private static final dbOperations dbconn = new dbOperations();
    private static boolean running = true;
    private static final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
   
    public static void main(String[] args) {
      
        
        Thread fileToDbThread = new Thread(() -> {
            while (running) {
                try {
                    if (dbconn.isDatabaseConnected()) {
                        fileHandler.uploadFileDataToDB(dbconn);
                    } else {
                        System.out.println("Database not connected. Waiting to retry...");
                    }
                    Thread.sleep(THREAD_SLEEP_TIME); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        fileToDbThread.start();

        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.print("Enter data packet (username,password) or 'exit': ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                running = false;
                break;
            }

            String[] packets = input.split("\n");
            for (String packet : packets) {
                String[] userDetails = packet.split(",");
                if (userDetails.length == 2) {
                    executor.execute(() -> {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + " running... Processing data packet: " + userDetails[0] + "," + userDetails[1]);
                        try {
                            if (dbconn.isDatabaseConnected()) {
                                dbconn.registerUser(userDetails[0], userDetails[1]);
                            } else {
                                fileHandler.storeUserData(userDetails[0], userDetails[1]);
                                System.out.println(threadName + ": Data stored in file due to database unavailability.");
                            }
                        } catch (Exception e) {
                            System.out.println(threadName + ": Error processing data packet: " + e.getMessage());
                        }
                    });
                    System.out.println("Data packet added for processing: " + userDetails[0] + "," + userDetails[1]);
                } else {
                    System.out.println("Invalid input format. Please enter in format 'username,password'.");
                }
            }
        }

        scanner.close();

     
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }

    
        fileToDbThread.interrupt();
        try {
            fileToDbThread.join();
        } catch (InterruptedException e) {
         }
       }

    }
    
  