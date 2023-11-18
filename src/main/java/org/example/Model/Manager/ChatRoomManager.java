package org.example.Model.Manager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;
import org.example.Model.MessageRes.Type.MessageRes;
import org.example.Model.MessageRes.Serializer.MessageSerializer;
import org.example.Model.MessageRes.Type.SCRoom;
import org.example.Model.Room;
import org.example.Model.User;

public class ChatRoomManager {

  private static ChatRoomManager instance;
  private final Map<Room, List<User>> roomManager;
  private MessageSerializer serializer;
  private Lock lock;
  private Condition condition;


  public ChatRoomManager() {
    this.roomManager = new HashMap<>();
  }

  public static synchronized ChatRoomManager getInstance() {
    if (instance == null) {
      instance = new ChatRoomManager();
    }
    return instance;
  }

  public void setSerializer(MessageSerializer serializer) {
    this.serializer = serializer;
  }

  public void setLock(Lock lock) {
    this.lock = lock;
    this.condition = lock.newCondition();
  }

  public List<User> getUserListById(int roomId) {
    List<User> list = null;
    for (Room room : roomManager.keySet()) {
      if (room.getId() == roomId) {
        list = roomManager.get(room);
      }
    }
    return list;
  }

  public Room findRoomByUserSocket(SocketChannel userSocket) {
    return roomManager.entrySet().stream()
        .filter(entry -> entry.getValue().stream()
            .anyMatch(user -> user.getSocketChannel() == userSocket))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);
  }

  public Optional<Room> findRoomByRoomId(int roomId) {
    for (Room room : roomManager.keySet()) {
      if (room.getId() == roomId) {
        return Optional.of(room);
      }
    }
    return Optional.empty();
  }

  public User findUser(SocketChannel clientSocket) {
    return roomManager.values().stream()
        .flatMap(List::stream)
        .filter(user -> user.getSocketChannel() == clientSocket)
        .findFirst()
        .orElseThrow();
  }

  public boolean checkUserInRoom(SocketChannel clientSocket) {
    return roomManager.values().stream()
        .flatMap(List::stream)
        .anyMatch(user -> user.getSocketChannel() == clientSocket);
  }

  public void addUserToChatRoom(Room room, User user) {
    List<User> userList = roomManager.computeIfAbsent(room, k -> new ArrayList<>());
    userList.add(user);
  }

  public void deleteUser(Room room, SocketChannel socketChannel) {
    roomManager.get(room).removeIf(user -> user.getSocketChannel() == socketChannel);
  }

  public boolean checkRoomIsEmpty(Room room){
    return roomManager.get(room).size() == 0;
  }
  
  public void deleteRoom(Room room) {
    roomManager.remove(room);
  }

  //방의 유저들에게 메세지 전체 방송
  public void broadcastMsgToRoom(int roomId, MessageRes messageRes) {
    List<User> userList = instance.getUserListById(roomId);
    if (userList != null) {
      userList.forEach(user -> sendMessage(user.getSocketChannel(), messageRes));
    } else {
      System.out.println("NULL이다 멍청아 ");
    }
  }

  //메세지 전송
  public void sendMessage(SocketChannel userSocket, MessageRes messageRes) {

    try {
      ByteBuffer byteBuffer = messageRes.accept(serializer);
      while (byteBuffer.hasRemaining()) {
        System.out.println("sent: "+ userSocket.write(byteBuffer));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //방이 존재하는지 확인
  public boolean isRoom(int roomId) {
    return roomManager.keySet()
        .stream()
        .anyMatch(room -> room.getId() == roomId);
  }

