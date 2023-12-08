package device;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Device {
  private String device_type;
  private String[] operations;
  private int id;
  private boolean isRegistered;

  public Device(String device_type) {
    this.device_type = device_type;
    this.operations = setOperations(device_type);
    this.id = (int) (Math.random() * 10000);
  }

  public String getDeviceType() {
    return this.device_type;
  }

  public String[] getOperations() {
    return this.operations;
  }

  public int getId() {
    return this.id;
  }

  public boolean isRegistered() {
    return this.isRegistered;
  }

  public void setRegistered(boolean isRegistered) {
    this.isRegistered = isRegistered;
  }

  public void randomizeId() {
    if (this.isRegistered) {
      System.err.println("Device is already registered, cannot change id");
      return;
    }

    this.id = (int) (Math.random() * 10000);
  }

  public void register(URL url) throws Exception {

    try {
      HttpClient client = HttpClient.newBuilder().build();

      JsonObject json = new JsonObject();
      json.addProperty("type", this.getDeviceType());
      json.addProperty("id", this.getId());
      json.addProperty("operations", new Gson().toJson(this.getOperations()));

      System.out.println(json.toString());

      HttpRequest request = HttpRequest.newBuilder()
          .uri(url.toURI())
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
          .build();

      client.sendAsync(request, BodyHandlers.ofString())
          .thenApply(HttpResponse::body)
          .thenAccept(System.out::println);

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      int status = response.statusCode();

      switch (status) {
        case 200:
          this.setRegistered(true);

          System.out.println(
              this.getDeviceType() +
                  " device registered with id " + this.getId() +
                  " and operations " + String.join(", ", this.getOperations()) +
                  " isRegistered: " + this.isRegistered());

          break;

        case 409:
          // If a device is already registered with the same id, generate a new id and try
          // again
          this.randomizeId();
          this.register(url);
          break;

        default:
          break;
      }

    } catch (Exception e) {
      System.out.println(e);
    }

  }

  private String[] setOperations(String device_type) {
    switch (device_type) {
      case "curtain":
        return new String[] { "open", "close" };
      case "air-conditioner":
        return new String[] { "on", "off" };
      case "tv":
        return new String[] { "on", "off", "volume_up", "volume_down", "channel_up", "channel_down" };
      case "sound-system":
        return new String[] { "on", "off", "volume_up", "volume_down" };
      case "light":
        return new String[] { "on", "off" };
      default:
        throw new IllegalArgumentException("Invalid device type");
    }
  }
}
