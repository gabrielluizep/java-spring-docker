package eng.tele.std.entitites;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import eng.tele.std.RabbitMqConnection;

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
    ConnectionFactory factory = RabbitMqConnection.getConnectionFactory();
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    String message = "setVolume-" + volume;
    channel.basicPublish("", "device-" + getId(), null, message.getBytes("UTF-8"));

    this.volume = volume;
  }

  public void setChannel(int channel) throws Exception {
    ConnectionFactory factory = RabbitMqConnection.getConnectionFactory();
    Connection connection = factory.newConnection();
    Channel brokerChannel = connection.createChannel();

    String message = "setChannel-" + channel;
    brokerChannel.basicPublish("device-" + this.getId(), "device-" + this.getId(), null, message.getBytes("UTF-8"));

    this.channel = channel;
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

    if (operation.startsWith("setChannel-")) {
      setChannel(Integer.parseInt(operation.split("-")[1]));
      return;
    }

    throw new IllegalArgumentException("Invalid operation: " + operation);
  }
}
