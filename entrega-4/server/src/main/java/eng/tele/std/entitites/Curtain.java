package eng.tele.std.entitites;

import eng.tele.std.StdApplication;

public class Curtain extends Device {
  private boolean isOpen;

  public Curtain(Device device) {
    super(device.getId(), device.getType(), device.getOperations());

    this.isOpen = false;
  }

  public boolean isOpen() {
    return isOpen;
  }

  public void setOpen(boolean isOpen) throws Exception {
    String message = isOpen ? "open" : "close";
    StdApplication.channel.basicPublish("", "device-" + getId(), null, message.getBytes());

    this.isOpen = isOpen;
  }

  @Override
  public void setOn(boolean isOn) throws Exception {
    setOpen(isOn);
  }

  @Override
  public void executeOperation(String operation) throws Exception {
    switch (operation) {
      case "open":
        setOpen(true);
        break;
      case "close":
        setOpen(false);
        break;
      default:
        throw new IllegalArgumentException("Invalid operation: " + operation);
    }
  }
}
