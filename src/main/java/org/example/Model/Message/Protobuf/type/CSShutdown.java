package org.example.Model.Message.Protobuf.type;

import java.nio.ByteBuffer;
import org.example.Model.Message.MessageTask;
import org.example.Model.Message.Protobuf.MessageProto.Type;

public class CSShutdown implements TypeProcessor {

  @Override
  public MessageTask processType(Type type, ByteBuffer byteBuffer) {
    MessageTask task = new MessageTask();
    task.setType("CSShutdown");
    return task;
  }
}
