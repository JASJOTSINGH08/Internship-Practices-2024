package myfirstapplicationwithdbhashmap;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchRecordin {

    public static String searchdatain(HashMap<String, String[]> list, String search) {
        StringBuilder result = new StringBuilder();
        for (String key : list.keySet()) {
            if (key.startsWith(search)) {
                result.append("Found entry: ").append(key).append("\n");
            }
        }
        if (result.length() == 0) {
            result.append("No entry found with IMEI ID: ").append(search);
        }
        return result.toString();
    }

    public static String searchdata(HashMap<String, ArrayList<String>> list, String searchIMEI) {
        StringBuilder result = new StringBuilder();
        for (String key : list.keySet()) {
            if (key.startsWith(searchIMEI)) {
                result.append("Found entry: ").append(key).append("\n");
            }
        }
        if (result.length() == 0) {
            result.append("No entry found with IMEI ID: ").append(searchIMEI);
        }
        return result.toString();
    }
}
