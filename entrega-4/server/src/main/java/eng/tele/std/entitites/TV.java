package eng.tele.std.entitites;

import eng.tele.std.StdApplication;

public class TV extends Device {
  private int volume;
  private int channel;

  public TV(Device device) {
    super(device.getId(), device.getType(), device.getOperations());

    this.volume = 0;
    this.channel = 0;
  }

  public int getVolume() {
    return volume;
  }

  public int getChannel() {
    return channel;
  }

  public void setVolume(int volume) throws Exception {
    String message = "setVolume-" + volume;
    StdApplication.channel.basicPublish("", "device-" + getId(), null, message.getBytes());

    this.volume = volume;
  }

  public void setChannel(int channel) throws Exception {
    String message = "setChannel-" + channel;
    StdApplication.channel.basicPublish("", "device-" + getId(), null, message.getBytes());

    this.channel = channel;
  }

  @Override
  public void executeOperation(String operation) throws Exception {
    switch (operation) {
      case "turnOn":
        setOn(true);
        break;
      case "turnOff":
        setOn(false);
        break;
      case "setVolume":
        setVolume(Integer.parseInt(operation.split(" ")[1]));
        break;
      case "setChannel":
        setChannel(Integer.parseInt(operation.split(" ")[1]));
        break;
      default:
        throw new IllegalArgumentException("Invalid operation: " + operation);
    }
  }
}
