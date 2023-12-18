package eng.tele.std.entitites;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import eng.tele.std.RabbitMqConnection;

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
    ConnectionFactory factory = RabbitMqConnection.getConnectionFactory();
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    String message = "setTemperature-" + temperature;
    channel.basicPublish("device-" + this.getId(), "device-" + this.getId(), null, message.getBytes("UTF-8"));

    this.temperature = temperature;
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

    if (operation.startsWith("setTemperature-")) {
      setTemperature(Integer.parseInt(operation.split("-")[1]));
      return;
    }

    throw new IllegalArgumentException("Invalid operation: " + operation);
  }
}
