public class HexToDecimal {
    public static void main(String[] args) {
        // IMEI Value in ASCII
        String IMEIval = "868994561234578";
        System.out.println("IMEI Value: " + IMEIval);

        // Convert HEX to decimal for Card ID
        String cardIdHex = "124532AE";
        long cardIdDecimal = Long.parseLong(cardIdHex, 16);
        System.out.println("Card ID Value : " + cardIdDecimal);

        // Date in HEX
        String dateHex = "3137303732303233";
        int day = Integer.parseInt(dateHex.substring(0, 2), 16);
        int month = Integer.parseInt(dateHex.substring(2, 4), 16);
        String year = hexToAscii(dateHex.substring(4, 12));

        // Time in HEX
        String timeHex = "313533303530";
        int hour = Integer.parseInt(timeHex.substring(0, 2), 16);
        int minute = Integer.parseInt(timeHex.substring(2, 4), 16);
        int second = Integer.parseInt(timeHex.substring(4, 6), 16);

        String dateAscii = hexToAscii(dateHex);
        String timeAscii = hexToAscii(timeHex);

        // Construct formatted strings for Date and Time
        String formattedDate = dateAscii.substring(0, 2) + "/" + dateAscii.substring(2, 4) + "/"
                + dateAscii.substring(4);
        String formattedTime = timeAscii.substring(0, 2) + ":" + timeAscii.substring(2, 4) + ":"
                + timeAscii.substring(4);

        System.out.println("Date ASCII Value: " + formattedDate);
        System.out.println("Time ASCII Value: " + formattedTime);
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
