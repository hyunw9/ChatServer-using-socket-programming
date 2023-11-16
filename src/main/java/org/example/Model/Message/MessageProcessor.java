package org.example.Model.Message;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface MessageProcessor {
  MessageTask processMessage(ByteBuffer buf) throws IOException;
  int getByteSize();
}
