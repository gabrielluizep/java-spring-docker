package eng.tele.std.entitites;

public class Light extends Device {
  public Light(Device device) {
    super(device.getId(), device.getType(), device.getOperations());
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
      default:
        throw new IllegalArgumentException("Invalid operation: " + operation);
    }
  }
}
