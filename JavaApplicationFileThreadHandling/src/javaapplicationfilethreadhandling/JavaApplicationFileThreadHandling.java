package javaapplicationfilethreadhandling;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JavaApplicationFileThreadHandling {
    private static final int MAX_THREADS = 3;
    private static final long THREAD_SLEEP_TIME = 20000; 
    private static final FileHandler fileHandler = new FileHandler();
    private static final DBFOperations dbconn = new DBFOperations();
    private static boolean running = true;
    private static final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

    public static void main(String[] args) {
        // Start a thread to handle file to DB upload
        Thread fileToDbThread = new Thread(() -> {
            while (running) {
                try {
                    if (dbconn.isDatabaseConnected()) {
                        System.out.println("Database connected. Uploading file data to DB...");
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
                                System.out.println(threadName + ": User registered successfully!");
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

        // Shutdown the executor after tasks are completed
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }

        // Interrupt the file to DB thread and wait for it to finish
        fileToDbThread.interrupt();
        try {
            fileToDbThread.join();
        } catch (InterruptedException e) {
        }
    }

}
