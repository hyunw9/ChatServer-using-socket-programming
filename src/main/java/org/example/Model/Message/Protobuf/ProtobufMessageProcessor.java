package org.example.Model.Message.Protobuf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
  private final Lock lock = new ReentrantLock();

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

  @Override
  public MessageTask processMessage(SocketChannel socketChannel) throws IOException {
    MessageTask task = null;
    int bytesRead = 0;
    short length = 0;
    ByteBuffer byteBuffer;
    Type type = null;

    //유저 2명이 동시에 메세지를 보냈을 때, type 뒤에 text 가 아닌,
    // 다른 유저의 type 이 읽히는 것을 방지하기 위하여 동기화 처리합니다.
    lock.lock();
    try {
      length = getMessageSize(socketChannel);
      byteBuffer = ByteBuffer.allocate(length);
      bytesRead = socketChannel.read(byteBuffer);  //type을 읽는다.

      if (bytesRead == length) {
        byteBuffer.flip();
        type = Type.parseFrom(byteBuffer.array());
      }

      if (type != null) {

        TypeProcessor typeProcessor = processors.get(type.getType()); //타입에 맞는 프로세서 생성

        length = getMessageSize(socketChannel); //다음 2바이트를 읽어 길이를 확인한다.

        if (length > 0) {
          byteBuffer = ByteBuffer.allocate(length);
          bytesRead = socketChannel.read(byteBuffer);

          if (bytesRead == length) {
            byteBuffer.flip();
          }
        } else {
          // 두 번째 메시지 길이가 0인 경우 byteBuffer를 null로 설정한다.
          byteBuffer = ByteBuffer.allocate(0);
        }

        task = typeProcessor.processType(type, byteBuffer); //Message로 변환
        task.setClientSocket(socketChannel);


      } else {
        System.out.println("원하는 만큼 못읽음");
      }

    } finally {
      lock.unlock();
    }
    return task;
  }
}
