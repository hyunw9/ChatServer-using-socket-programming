package org.example.Model;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class ChatRoomManager {

  private Map<String, Room> roomManager;

  public ChatRoomManager() {
    this.roomManager = new HashMap<>();
  }

  public void createRoom(String roomId) {
    Room room = new Room();
    roomManager.put(roomId, room);
  }

  public void addUserToChatRoom(String roomId, SocketChannel user) {
    Room room = roomManager.get(roomId);
    if (room != null) {
      room.addUser(user);
    }
  }

  public void deleteRoom(String roomId) {
    roomManager.remove(roomId);

  }
}
