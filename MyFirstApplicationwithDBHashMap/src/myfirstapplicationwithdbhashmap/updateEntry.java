/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myfirstapplicationwithdbhashmap;

/**
 *
 * @author Dell
 */

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
    
public class updateEntry {
    
public static void updateEntry(HashMap<String, ArrayList<String>> list, String IMEI) {
    Scanner scanner = new Scanner(System.in);

    ArrayList<String> entry = list.get(IMEI);
    if (entry != null) {
        System.out.println("Current entry:");
        System.out.println("IMEIval: " + entry.get(0) + ", cardIdHex: " + entry.get(1) + ", dateHex: " + entry.get(2) );

        System.out.print("Enter new CARDIDHEX (or press Enter to keep existing value): ");
        String newCardIdHex = scanner.nextLine();
        System.out.print("Enter new DATEHEX (or press Enter to keep existing value): ");
        String newDateHex = scanner.nextLine();

        // Check if all required fields are provided
        if (!newCardIdHex.isEmpty() || !newDateHex.isEmpty() ) {
            if (!newCardIdHex.isEmpty()) {
                entry.set(1, newCardIdHex);
            }
            if (!newDateHex.isEmpty()) {
                entry.set(2, newDateHex);
            }

            list.put(IMEI, entry); // Update the collection with the modified entry

            System.out.println("Entry updated successfully.");
        } else {
            System.out.println("No new data provided for update.");
        }
    } else {
        System.out.println("No entry found with IMEI: " + IMEI);
    }
}


}

