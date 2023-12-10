package eng.tele.std.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eng.tele.std.DataStore;
import eng.tele.std.entitites.Room;

@RestController
@RequestMapping(path = "/rooms", consumes = "application/json", produces = "application/json")
public class RoomsController {
  @PostMapping
  public ResponseEntity<Void> createRoom(@RequestBody String body) {
    DataStore dataStore = DataStore.getInstance();

    Gson gson = new Gson();
    JsonObject jsonBody = gson.fromJson(body, JsonObject.class);
    String name = jsonBody.get("name").getAsString();

    if (dataStore.getRoomByName(name) != null) {
      System.out.println("Cannot create room with name '" + name + "' because it already exists");
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    List<Integer> devicesIds = new ArrayList<>();
    JsonArray jsonDevicesIds = jsonBody.get("devices").getAsJsonArray();
    for (JsonElement jsonDeviceId : jsonDevicesIds) {
      devicesIds.add(jsonDeviceId.getAsInt());
    }

    for (Integer deviceId : devicesIds) {
      if (dataStore.getDeviceById(deviceId) == null) {
        System.out.println("Cannot create room with name '" + name + "' because device with id '" + deviceId
            + "' does not exist");
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
      }
    }

    Room room = new Room(name, devicesIds);

    dataStore.addRoom(room);

    System.out.println("POST /rooms\n" + "Room with name '" + name + "' created");
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<String> getRooms() {
    List<Room> rooms = DataStore.getInstance().getRooms();

    if (rooms.isEmpty()) {
      System.out.println("GET /rooms\n" + "No rooms registered");
      return ResponseEntity.notFound().build();
    }

    JsonArray jsonDevices = new JsonArray();
    for (Room room : rooms) {
      jsonDevices.add(room.toJson());
    }

    System.out.println("GET /rooms\n" + jsonDevices.toString());
    return ResponseEntity.ok(jsonDevices.toString());
  }

  @GetMapping(path = "/{name}")
  public ResponseEntity<String> getRoom(@PathVariable String name) {
    List<Room> rooms = DataStore.getInstance().getRooms();

    for (Room room : rooms) {
      if (room.getName().equals(name)) {
        System.out.println("GET /rooms/" + name + "\n" + room.toJson().toString());
        return ResponseEntity.ok(room.toJson().toString());
      }
    }

    return ResponseEntity.notFound().build();
  }

  @PutMapping(path = "/{name}")
  public ResponseEntity<Void> updateRoom(@PathVariable String name, @RequestBody String body) {
    Gson gson = new Gson();
    JsonObject jsonBody = gson.fromJson(body, JsonObject.class);

    Room room = DataStore.getInstance().getRoomByName(name);

    if (room == null) {
      System.out.println("PUT /rooms/" + name + "\n" + "Room with name " + name + " not found");
      return ResponseEntity.notFound().build();
    }

    boolean hasPowerAttribute = jsonBody.has("power");
    boolean hasDevicesAttribute = jsonBody.has("devices");

    if (hasPowerAttribute) {
      boolean power = jsonBody.get("power").getAsBoolean();
      try {

        room.setPower(power);
      } catch (Exception e) {
        System.out.println("PUT /rooms/" + name + "\n" + "Room with name " + name + " does not support operation "
            + (power ? "turnOn" : "turnOff"));
        return ResponseEntity.badRequest().build();
      }
    }

    if (hasDevicesAttribute) {
      List<Integer> devicesIds = new ArrayList<>();
      JsonArray jsonDevicesIds = jsonBody.get("devices").getAsJsonArray();
      for (JsonElement jsonDeviceId : jsonDevicesIds) {
        devicesIds.add(jsonDeviceId.getAsInt());
      }
      room.setDevicesIds(devicesIds);
    }

    if (!hasPowerAttribute && !hasDevicesAttribute) {
      System.out.println("PUT /rooms/" + name + "\n" + "No attributes to update");
      return ResponseEntity.badRequest().build();
    }

    System.out.println("PUT /rooms/" + name + "\n" + "Room with name " + name + " updated");
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(path = "/{name}")
  public ResponseEntity<Void> deleteRoom(@PathVariable String name) {
    Room room = DataStore.getInstance().getRoomByName(name);

    if (room == null) {
      System.out.println("DELETE /rooms/" + name + "\n" + "Room with name " + name + " not found");
      return ResponseEntity.notFound().build();
    }

    DataStore.getInstance().deleteRoom(room);

    System.out.println("DELETE /rooms/" + name + "\n" + "Room with name " + name + " deleted");
    return ResponseEntity.ok().build();
  }

}
