package device;

public class Device {
  private String device_type;
  private String[] operations;
  private int id;

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
