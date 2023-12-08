package device;

import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import device.Device;

public class App {
    public static final String CURTAIN = "curtain";
    public static final String AIR_CONDITIONER = "air-conditioner";
    public static final String TV = "tv";
    public static final String SOUND_SYSTEM = "sound-system";
    public static final String LIGHT = "light";
    public static final String[] DEVICE_TYPES = { CURTAIN, AIR_CONDITIONER, TV, SOUND_SYSTEM, LIGHT };

    public static final String REGISTER_ROUTE = "/register";

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.err.println("Two arguments expected: SERVER_URL and DEVICE_TYPE");
            return;
        }

        String server_url = args[0];
        if (!server_url.matches("^(http|https)://.*$")) {
            System.err.println("SERVER_URL must be a valid URL");
            return;
        }

        String device_type = args[1];
        if (!device_type.matches("^(curtain|air-conditioner|tv|sound-system|light)$")) {
            System.err.println("DEVICE_TYPE must be one of: " + String.join(", ", DEVICE_TYPES) + ".");
            return;
        }

        Device device = new Device(device_type);

        try {
            URL url = new URL(server_url + REGISTER_ROUTE);
            device.register(url);
        } catch (Exception e) {
            System.err.println("Device registration failed");
            System.err.println(e);
        }
    }
}
