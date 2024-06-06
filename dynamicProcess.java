import java.util.Scanner;

public class dynamicProcess {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter data (format: IMEI,CARDIDHEX,DATEHEX,TIMEHEX#): ");
        String input = scanner.nextLine();

        if (input.endsWith("#")) {
            input = input.substring(0, input.length() - 1);
        }

        String[] data = input.split(",");

        if (data.length != 4) {
            System.out.println(
                    "Invalid input format. Please provide data in the format: IMEI,CARDIDHEX,DATEHEX,TIMEHEX#");
            return;
        }
        String IMEIval = data[0];
        String cardIdHex = data[1];
        String dateHex = data[2];
        String timeHex = data[3];

        long cardIdDecimal = Long.parseLong(cardIdHex, 16);

        int day = Integer.parseInt(dateHex.substring(0, 2), 16);
        int month = Integer.parseInt(dateHex.substring(2, 4), 16);
        String year = hexToAscii(dateHex.substring(4, 12));

        int hour = Integer.parseInt(timeHex.substring(0, 2), 16);
        int minute = Integer.parseInt(timeHex.substring(2, 4), 16);
        int second = Integer.parseInt(timeHex.substring(4, 6), 16);

        String dateAscii = hexToAscii(dateHex);
        String timeAscii = hexToAscii(timeHex);

        String formattedDate = dateAscii.substring(0, 2) + "/" + dateAscii.substring(2, 4) + "/"
                + dateAscii.substring(4);
        String formattedTime = timeAscii.substring(0, 2) + ":" + timeAscii.substring(2, 4) + ":"
                + timeAscii.substring(4);

        System.out.println("IMEI Value: " + IMEIval);
        System.out.println("Card ID Value: " + cardIdDecimal);
        System.out.println("Date ASCII Value: " + formattedDate);
        System.out.println("Time ASCII Value: " + formattedTime);

        scanner.close();
    }

    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }
}
