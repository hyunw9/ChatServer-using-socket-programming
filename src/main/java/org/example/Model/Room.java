package org.example.Model;

import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

public class Room {

  private final Set<SocketChannel> room;

  public Room() {
    this.room = new HashSet<>();
  }

  public void addUser(SocketChannel user){
    this.room.add(user);
  }

  public void removeUser(SocketChannel user){
    this.room.remove(user);
  }

  public Set<SocketChannel> getChatRoomManager() {
    return room;
  }

}
