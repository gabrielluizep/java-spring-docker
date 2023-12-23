package eng.tele.std.entitites;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import eng.tele.std.RabbitMqConnection;

public class Device {
  private int id;
  private String type;
  private List<String> operations;
  private boolean isOn;

  public Device() {
  }

  public Device(int id, String type, List<String> operations) {
    this.id = id;
    this.type = type;
    this.operations = operations;
  }

  public int getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public List<String> getOperations() {
    return operations;
  }

  public boolean isOn() {
    return isOn;
  }

  public void setOn(boolean isOn) throws Exception {
    ConnectionFactory factory = RabbitMqConnection.getConnectionFactory();
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    String message = isOn ? "turnOn" : "turnOff";
    channel.basicPublish("device-" + this.getId(), "device-" + this.getId(), null, message.getBytes("UTF-8"));

    this.isOn = isOn;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json.addProperty("id", id);
    json.addProperty("type", type);
    JsonArray jsonOperations = new JsonArray();
    for (String operation : operations) {
      jsonOperations.add(operation);
    }
    json.add("operations", jsonOperations);

    return json;
  }

  public void executeOperation(String operation) throws Exception {
    return;
  };
}
