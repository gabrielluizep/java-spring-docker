package eng.tele.std.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

import eng.tele.std.DataStore;
import eng.tele.std.RabbitMqConnection;
import eng.tele.std.entitites.AirConditioner;
import eng.tele.std.entitites.Curtain;
import eng.tele.std.entitites.Device;
import eng.tele.std.entitites.Light;
import eng.tele.std.entitites.SoundSystem;
import eng.tele.std.entitites.TV;

@RestController
@RequestMapping(path = "/register", consumes = "application/json")
public class RegisterController {
  @PostMapping
  public ResponseEntity<Void> register(@RequestBody Device device) throws Exception {
    DataStore dataStore = DataStore.getInstance();

    if (dataStore.getDeviceById(device.getId()) != null) {
      System.out.println("Device with id " + device.getId() + " already registered");
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    try {
      ConnectionFactory factory = RabbitMqConnection.getConnectionFactory();
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();

      channel.exchangeDeclare("device-" + device.getId(), "fanout");
    } catch (Exception e) {
      System.out.println("POST /register\n" + "Device with id " + device.getId() + " not registered");
      System.err.println(e.toString());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    switch (device.getType()) {
      case "curtain":
        dataStore.addDevice(new Curtain(device));
        break;
      case "light":
        dataStore.addDevice(new Light(device));
        break;
      case "sound-system":
        dataStore.addDevice(new SoundSystem(device));
        break;
      case "air-conditioner":
        dataStore.addDevice(new AirConditioner(device));
        break;
      case "tv":
        dataStore.addDevice(new TV(device));
        break;
      default:
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    System.out.println(
        "POST /register\n" + "Device, of type " + device.getType() + " with id " + device.getId() + " registered");
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
