package org.example.Model.Message.Protobuf.type;

import com.google.protobuf.InvalidProtocolBufferException;
import java.nio.ByteBuffer;
import org.example.Model.Message.MessageTask;
import org.example.Model.Message.Protobuf.MessageProto;
import org.example.Model.Message.Protobuf.MessageProto.Type;

public class CSJoinRoom implements TypeProcessor {

  @Override
  public MessageTask processType(Type type, ByteBuffer byteBuffer)
      throws InvalidProtocolBufferException {

    MessageTask task = new MessageTask();
    MessageProto.CSJoinRoom csJoinRoom = MessageProto.CSJoinRoom.parseFrom(byteBuffer.array());
    task.setType("CSJoinRoom");
    task.setRoomId(String.valueOf(csJoinRoom.getRoomId()));
    return task;
  }
}

