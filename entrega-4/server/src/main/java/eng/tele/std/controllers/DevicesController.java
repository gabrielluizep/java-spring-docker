package eng.tele.std.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import eng.tele.std.DataStore;
import eng.tele.std.entitites.Device;

@RestController
@RequestMapping(path = "/devices", consumes = "application/json", produces = "application/json")
public class DevicesController {
  @GetMapping
  public ResponseEntity<String> getDevices() {
    List<Device> devices = DataStore.getInstance().getDevices();

    if (devices.isEmpty()) {
      System.out.println("GET /devices\n" + "No devices registered");
      return ResponseEntity.notFound().build();
    }

    JsonArray jsonDevices = new JsonArray();
    for (Device device : devices) {
      jsonDevices.add(device.toJson());
    }

    System.out.println("GET /devices\n" + jsonDevices.toString());
    return ResponseEntity.ok(jsonDevices.toString());
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<String> getDevice(@PathVariable int id) {
    List<Device> devices = DataStore.getInstance().getDevices();

    for (Device device : devices) {
      System.out.println(device.getId());
      if (device.getId() == id) {
        System.out.println("GET /devices/" + id + "\n" + device.toJson().toString());
        return ResponseEntity.ok(device.toJson().toString());
      }
    }

    System.out.println("GET /devices/" + id + "\n" + "Device with id " + id + " not found");
    return ResponseEntity.notFound().build();
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<Void> updateDevice(@PathVariable int id, @RequestBody String body) {
    Gson gson = new Gson();
    JsonObject jsonBody = gson.fromJson(body, JsonObject.class);
    String operation = jsonBody.get("operation").getAsString();

    Device device = DataStore.getInstance().getDeviceById(id);

    if (device == null) {
      System.out.println("PUT /devices/" + id + "\n" + "Device with id " + id + " not found");
      return ResponseEntity.notFound().build();
    }
    try {
      device.executeOperation(operation);
    } catch (Exception e) {
      System.out.println("PUT /devices/" + id + "\n" + "Device with id " + id + " does not support operation "
          + operation);
      System.out.println(e.toString());
      return ResponseEntity.badRequest().build();
    }

    System.out.println("PUT /devices/" + id + "\n" + "Device with id " + id + " updated");
    return ResponseEntity.ok().build();
  }
}
