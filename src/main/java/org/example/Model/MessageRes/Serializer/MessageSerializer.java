package org.example.Model.MessageRes.Serializer;

import com.google.protobuf.InvalidProtocolBufferException;
import java.nio.ByteBuffer;
import org.example.Model.MessageRes.Type.SCChatRes;
import org.example.Model.MessageRes.Type.SCRoomListRes;
import org.example.Model.MessageRes.Type.SCSystemMessageRes;

public interface MessageSerializer {

  ByteBuffer serialize(SCChatRes message) throws InvalidProtocolBufferException;
  ByteBuffer serialize(SCRoomListRes message);
  ByteBuffer serialize(SCSystemMessageRes message);

}
