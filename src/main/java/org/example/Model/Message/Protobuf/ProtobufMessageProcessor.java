package org.example.Model.Message.Protobuf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import org.example.Model.Message.MessageProcessor;
import org.example.Model.Message.MessageTask;
import org.example.Model.Message.Protobuf.MessageProto.Type;
import org.example.Model.Message.Protobuf.MessageProto.Type.MessageType;
import org.example.Model.Message.Protobuf.type.CSChat;
import org.example.Model.Message.Protobuf.type.CSCreateRoom;
import org.example.Model.Message.Protobuf.type.CSJoinRoom;
import org.example.Model.Message.Protobuf.type.CSLeaveRoom;
import org.example.Model.Message.Protobuf.type.CSName;
import org.example.Model.Message.Protobuf.type.CSRooms;
import org.example.Model.Message.Protobuf.type.CSShutdown;
import org.example.Model.Message.Protobuf.type.TypeProcessor;

public class ProtobufMessageProcessor implements MessageProcessor {

  private static final Map<MessageType, TypeProcessor> processors = new HashMap<>();

  public ProtobufMessageProcessor() {
    processors.put(MessageType.CS_NAME, new CSName());
    processors.put(MessageType.CS_ROOMS, new CSRooms());
    processors.put(MessageType.CS_CREATE_ROOM, new CSCreateRoom());
    processors.put(MessageType.CS_JOIN_ROOM, new CSJoinRoom());
    processors.put(MessageType.CS_LEAVE_ROOM, new CSLeaveRoom());
    processors.put(MessageType.CS_CHAT, new CSChat());
    processors.put(MessageType.CS_SHUTDOWN, new CSShutdown());
  }

