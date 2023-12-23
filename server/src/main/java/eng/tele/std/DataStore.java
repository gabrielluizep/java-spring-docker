package eng.tele.std;

import java.util.ArrayList;
import java.util.List;

import eng.tele.std.entitites.Device;
import eng.tele.std.entitites.Room;

public class DataStore {

  private List<Device> devices;
  private List<Room> rooms;

  private DataStore() {
    this.devices = new ArrayList<Device>();
    this.rooms = new ArrayList<Room>();
  }

  private static final DataStore INSTANCE = new DataStore();

  public static DataStore getInstance() {
    return INSTANCE;
  }

  public List<Device> getDevices() {
    return devices;
  }

  public Device getDeviceById(int id) {
    for (Device device : devices) {
      if (device.getId() == id) {
        return device;
      }
    }

    return null;
  }

  public void addDevice(Device device) {
    devices.add(device);
  }

  public List<Room> getRooms() {
    return rooms;
  }

  public Room getRoomByName(String name) {
    for (Room room : rooms) {
      if (room.getName().equals(name)) {
        return room;
      }
    }

    return null;
  }

  public void addRoom(Room room) {
    rooms.add(room);
  }

  public void deleteRoom(Room room) {
    rooms.remove(room);
  }
}