package org.example.Model.MessageRes.Type;

import java.nio.ByteBuffer;
import org.example.Model.MessageRes.Serializer.MessageSerializer;

public interface MessageRes {
    
  String getType();
  ByteBuffer accept(MessageSerializer serializer);
}
