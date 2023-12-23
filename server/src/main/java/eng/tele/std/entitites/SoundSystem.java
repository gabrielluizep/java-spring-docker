package eng.tele.std.entitites;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import eng.tele.std.RabbitMqConnection;

public class SoundSystem extends Device {
  private int volume;

  public SoundSystem(Device device) {
    super(device.getId(), device.getType(), device.getOperations());

    this.volume = 0;
  }

  public int getVolume() {
    return volume;
  }

  public void setVolume(int volume) throws Exception {
    ConnectionFactory factory = RabbitMqConnection.getConnectionFactory();
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    String message = "setVolume-" + volume;
    channel.basicPublish("device-" + this.getId(), "device-" + this.getId(), null, message.getBytes("UTF-8"));

    this.volume = volume;
  }

  @Override
  public void executeOperation(String operation) throws Exception {
    if (operation.equals("turnOn")) {
      setOn(true);
      return;
    }

    if (operation.equals("turnOff")) {
      setOn(false);
      return;
    }

    if (operation.startsWith("setVolume-")) {
      setVolume(Integer.parseInt(operation.split("-")[1]));
      return;
    }

    throw new IllegalArgumentException("Invalid operation: " + operation);
  }
}
