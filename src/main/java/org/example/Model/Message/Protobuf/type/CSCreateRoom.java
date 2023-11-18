package org.example.Model.Message.Protobuf.type;

import com.google.protobuf.InvalidProtocolBufferException;
import java.nio.ByteBuffer;
import org.example.Model.Message.MessageTask;
import org.example.Model.Message.Protobuf.MessageProto;
import org.example.Model.Message.Protobuf.MessageProto.Type;

public class CSCreateRoom implements TypeProcessor {

  @Override
  public MessageTask processType(Type type, ByteBuffer byteBuffer)
      throws InvalidProtocolBufferException {

      MessageTask task = new MessageTask();
      MessageProto.CSCreateRoom csCreateRoom = MessageProto.CSCreateRoom.parseFrom(byteBuffer.array());
      task.setType("CSCreateRoom");
      task.setTitle(csCreateRoom.getTitle());
      return task;
  }
}
