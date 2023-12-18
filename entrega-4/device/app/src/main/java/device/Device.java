package device;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Device {
  private String device_type;
  private List<String> operations;
  private int id;
  private boolean isRegistered;

  public Device(String device_type) {
    this.device_type = device_type;
    this.operations = setOperations(device_type);
    this.id = (int) (Math.random() * 10000);
  }

  public String getDeviceType() {
    return this.device_type;
  }

  public List<String> getOperations() {
    return this.operations;
  }

  public int getId() {
    return this.id;
  }

  public boolean isRegistered() {
    return this.isRegistered;
  }

  public void setRegistered(boolean isRegistered) {
    this.isRegistered = isRegistered;
  }

  public void randomizeId() {
    if (this.isRegistered) {
      System.err.println("Device is already registered, cannot change id");
      return;
    }

    this.id = (int) (Math.random() * 10000);
  }

  public void register(URL url) throws Exception {
    HttpClient client = HttpClient.newBuilder().build();

    JsonObject json = new JsonObject();
    json.addProperty("type", this.getDeviceType());
    json.addProperty("id", this.getId());
    JsonArray operationsArray = new JsonArray();
    for (String operation : operations) {
      operationsArray.add(operation);
    }
    json.add("operations", operationsArray);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(url.toURI())
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    int status = response.statusCode();

    switch (status) {
      case 200:
        this.setRegistered(true);

        System.out.println(
            this.getDeviceType() +
                " device registered with id " + this.getId() +
                " and operations " + String.join(", ", this.getOperations()) +
                " isRegistered: " + this.isRegistered());

        // setup rabitmq
        ConnectionFactory factory = RabbitMqConnection.getConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("device-" + this.getId(), "fanout");
        String queueName = channel.queueDeclare("device-" + this.getId(), false, false, false, null).getQueue();
        channel.queueBind(queueName, "device-" + this.getId(), "");
        System.out.println(" [*] " + this.getId() + " Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(
              String consumerTag,
              Envelope envelope,
              AMQP.BasicProperties properties,
              byte[] body)
              throws IOException {
            String message = new String(body, "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
          }
        };

        channel.basicConsume(queueName, true, consumer);
        break;

      case 409:
        System.out.println("Device with id " + this.getId() + " already registered, generating new id");
        // If a device is already registered with the same id,
        // generate a new id and try again
        this.randomizeId();
        this.register(url);
        break;

      default:
        throw new Exception("Device registration failed with status " + status);
    }
  }

  private List<String> setOperations(String device_type) {
    switch (device_type) {
      case "curtain":
        return new ArrayList<String>(Arrays.asList("open", "close"));
      case "air-conditioner":
        return new ArrayList<String>(Arrays.asList("turnOn", "turnOff"));
      case "tv":
        return new ArrayList<String>(
            Arrays.asList("turnOn", "turnOff", "setVolume", "setChannel"));
      case "sound-system":
        return new ArrayList<String>(Arrays.asList("turnOn", "turnOff", "setVolume"));
      case "light":
        return new ArrayList<String>(Arrays.asList("turnOn", "turnOff"));
      default:
        throw new IllegalArgumentException("Invalid device type");
    }
  }
}
