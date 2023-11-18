package org.example.Model.Message.Protobuf.type;

import com.google.protobuf.InvalidProtocolBufferException;
import java.nio.ByteBuffer;
import org.example.Model.Message.MessageTask;
import org.example.Model.Message.Protobuf.MessageProto;
import org.example.Model.Message.Protobuf.MessageProto.Type;

public class CSChat implements TypeProcessor {

  @Override
  public MessageTask processType(Type type, ByteBuffer byteBuffer)
      throws InvalidProtocolBufferException {
    MessageTask task = new MessageTask();
    MessageProto.CSChat csChat = MessageProto.CSChat.parseFrom(byteBuffer.array());
    task.setType("CSChat");
    task.setText(csChat.getText());
    return task;
  }
}
