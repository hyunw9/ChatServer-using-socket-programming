package org.example.Model.Message.Protobuf.type;

import com.google.protobuf.InvalidProtocolBufferException;
import java.nio.ByteBuffer;
import org.example.Model.Message.MessageTask;
import org.example.Model.Message.Protobuf.MessageProto.Type;

public interface TypeProcessor {
  MessageTask processType(Type type,ByteBuffer byteBuffer) throws InvalidProtocolBufferException;

}
