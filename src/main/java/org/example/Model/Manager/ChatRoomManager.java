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

