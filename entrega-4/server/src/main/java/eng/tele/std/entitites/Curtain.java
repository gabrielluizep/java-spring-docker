package eng.tele.std.entitites;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import eng.tele.std.RabbitMqConnection;

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
    ConnectionFactory factory = RabbitMqConnection.getConnectionFactory();
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    String message = isOpen ? "open" : "close";
    channel.basicPublish("device-" + getId(), "device-" + getId(), null, message.getBytes("UTF-8"));

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
