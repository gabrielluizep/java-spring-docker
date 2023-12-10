package eng.tele.std.entitites;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import eng.tele.std.DataStore;

public class Room {
  private String name;
  private List<Integer> devicesIds;

  public Room(String name, List<Integer> devicesIds) {
    this.name = name;
    this.devicesIds = devicesIds;
  }

  public String getName() {
    return name;
  }

  public List<Integer> getDevicesIds() {
    return devicesIds;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDevicesIds(List<Integer> devicesIds) {
    this.devicesIds = devicesIds;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json.addProperty("name", name);

    JsonArray jsonDevicesIds = new JsonArray();
    for (Integer deviceId : devicesIds) {
      jsonDevicesIds.add(deviceId);
    }
    json.add("devices", jsonDevicesIds);

    return json;
  }

  public void setPower(boolean power) throws Exception {
    List<Device> devices = DataStore.getInstance().getDevices();

    for (Device device : devices) {
      if (devicesIds.contains(device.getId())) {
        device.setOn(power);
      }
    }

  }
}
