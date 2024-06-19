package javadeviceapplication;

import java.util.List;
import java.util.Map;

public class JavaDeviceApplication {

    public static void main(String[] args) {
        DBDeviceConnection dbConn = new DBDeviceConnection();
        
        List<Map<String, Object>> devices = dbConn.getDevices();
        for (Map<String, Object> device : devices) {
            String imeiVal = (String) device.get("IMEIval");
            List<Map<String, Object>> settings = dbConn.getDeviceSettings(imeiVal);
            Map<String, Object> role = dbConn.getDeviceRole(imeiVal);
            
            device.put("settings", settings);
            device.put("role", role);
        }
        
        // Display merged data
        for (Map<String, Object> device : devices) {
            System.out.println("id: " + device.get("id"));
            System.out.println("IMEIval: " + device.get("IMEIval"));

            List<Map<String, Object>> settings = (List<Map<String, Object>>) device.get("settings");
            for (Map<String, Object> setting : settings) {
                System.out.println("DeviceSetting: " + setting.get("Devicetypes"));
            }

            Map<String, Object> role = (Map<String, Object>) device.get("role");
            System.out.println("DeviceRole: " + role.get("Devicerlo"));
            System.out.println();
        }
    }
}