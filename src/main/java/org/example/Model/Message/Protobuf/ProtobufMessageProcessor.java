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

  public static short getMessageSize(SocketChannel socketChannel) throws IOException {
    short size = 0;
    ByteBuffer byteBuffer = ByteBuffer.allocate(2);
    int bytesRead = socketChannel.read(byteBuffer);
    byteBuffer.flip();
    if (bytesRead == 2) {
      size = byteBuffer.getShort();
    }

    try {
      if (bytesRead == -1) {
        throw new InterruptedException();

      }
    } catch (InterruptedException e) {
      System.out.println("올바르지 않은 입력, 스레드 인터럽트 발생, 클라이언트 연결 종료");
      socketChannel.close();

    }
    return size;
  }

