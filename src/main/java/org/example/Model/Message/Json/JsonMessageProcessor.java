package org.example.Model.Message.Json;

import static org.example.ServerController.output;
import static org.example.ServerController.shutdownServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import org.example.Model.Message.MessageProcessor;
import org.example.Model.Message.MessageTask;

public class JsonMessageProcessor implements MessageProcessor {

  //앞 2바이트를 읽어, 메세지 크기를 알아내는 메소드
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

