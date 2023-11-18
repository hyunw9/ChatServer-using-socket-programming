package org.example.Model.Message;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface MessageProcessor {
  MessageTask processMessage(SocketChannel socketChannel) throws IOException;
}
