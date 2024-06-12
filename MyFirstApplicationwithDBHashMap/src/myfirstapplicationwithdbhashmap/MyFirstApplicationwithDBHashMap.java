package myfirstapplicationwithdbhashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MyFirstApplicationwithDBHashMap {
    private static final HashMap<String, ArrayList<String>> list = new HashMap<>();

    public static void main(String[] args) {
        DatabaseOperationsConn dbOps = new DatabaseOperationsConn();
        Scanner scanner = new Scanner(System.in);

        // Fetch existing data from the database and populate the collection
        HashMap<String, ArrayList<String>> existingData = dbOps.fetchDataMap();
        list.putAll(existingData);

        while (true) {
            System.out.print("Enter data (format: IMEI,CARDIDHEX,DATEHEX,TIMEHEX# or type 'exit' to quit): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            if (input.endsWith("#")) {
                input = input.substring(0, input.length() - 1);
            }

            String[] items = input.split(",");
            if (items.length != 4) {
                System.out.println("Invalid input format. Please provide data in the format: IMEI,CARDIDHEX,DATEHEX,TIMEHEX#");
                continue;
            }

            String IMEIval = items[0];
            String cardIdHex = items[1];
            String dateHex = items[2];
            String timeHex = items[3];

            long cardIdDecimal;
            try {
                cardIdDecimal = Long.parseLong(cardIdHex, 16);
            } catch (NumberFormatException e) {
                System.out.println("Invalid cardIdHex format. Please provide a valid hexadecimal number.");
                continue;
            }

            String dateAscii = DataConversionch.hexToAscii(dateHex);
            String timeAscii = DataConversionch.hexToAscii(timeHex);

            dbOps.insertData(IMEIval, cardIdDecimal, dateAscii, timeAscii);

            ArrayList<String> dataListEntry = new ArrayList<>();
            dataListEntry.add(IMEIval);
            dataListEntry.add(cardIdHex);
            dataListEntry.add(dateHex);
            dataListEntry.add(timeHex);

            list.put(IMEIval, dataListEntry);

            System.out.println("Current map: " + list);
        }

        System.out.print("Do you want to print the data? (yes/no): ");
        String printOption = scanner.nextLine();
        if (printOption.equalsIgnoreCase("yes")) {
            printData(list);
        }

        System.out.print("Do you want to search by IMEI? (yes/no): ");
        String searchOption = scanner.nextLine();
        if (searchOption.equalsIgnoreCase("yes")) {
            System.out.print("Enter IMEI ID to search: ");
            String searchIMEI = scanner.nextLine();
            String result = SearchRecordin.searchdata(list, searchIMEI);
            System.out.println(result);
           
        System.out.println("Do you want to update this entry? (yes/no): ");
         String updateOption = scanner.nextLine();
        if (updateOption.equalsIgnoreCase("yes")){
            updateEntry.updateEntry(list,searchIMEI);
        }
        
        }
        
     scanner.close();
     
    }

    private static void printData(HashMap<String, ArrayList<String>> map) {
        System.out.println("Data from the collection map:");
        for (String key : map.keySet()) {
            ArrayList<String> items = map.get(key);
            System.out.println("IMEIval: " + items.get(0) + ", cardIdHex: " + items.get(1) + ", dateHex: " + items.get(2)) ;
            System.out.println("__________________________________________________________________________________________________________");
        }
    }
}
