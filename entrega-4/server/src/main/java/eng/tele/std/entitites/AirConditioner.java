package eng.tele.std.entitites;

import eng.tele.std.StdApplication;

public class AirConditioner extends Device {
  private int temperature;

  public AirConditioner(Device device) {
    super(device.getId(), device.getType(), device.getOperations());

    this.temperature = 20;
  }

  public int getTemperature() {
    return temperature;
  }

  public void setTemperature(int temperature) throws Exception {
    String message = "setTemperature-" + temperature;
    StdApplication.channel.basicPublish("", "device-" + getId(), null, message.getBytes());

    this.temperature = temperature;
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
      case "setTemperature":
        setTemperature(Integer.parseInt(operation.split(" ")[1]));
        break;
      default:
        throw new IllegalArgumentException("Invalid operation: " + operation);
    }
  }
}
