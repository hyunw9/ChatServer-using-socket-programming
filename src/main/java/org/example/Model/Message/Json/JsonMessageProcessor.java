package org.example.Model.Message.Json;

import static org.example.ServerController.output;

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

  @Override
  public MessageTask processMessage(SocketChannel socketChannel) throws IOException {

    //알아낸 크기만큼 Read 합니다.
    short size = getMessageSize(socketChannel);
    ByteBuffer buf = ByteBuffer.allocate(size);
    int bytesRead = socketChannel.read(buf);

    buf.flip();
    String msg = StandardCharsets.UTF_8.decode(buf).toString();

    msg = msg.substring(msg.indexOf("{"), msg.lastIndexOf("}") + 1);
    output.print(msg);

    ObjectMapper mapper = new ObjectMapper().configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    //Message의 Type 별로 내부 데이터를 설정하고 반환합니다.
    MessageTask task = mapper.readValue(msg, MessageTask.class);
    task.setClientSocket(socketChannel);
    if (task.getType().equals("CSName")) {
      task.setData(task.getName());
    } else if (task.getType().equals("CSJoinRoom")) {
      task.setData(task.getRoomId());
    } else if (task.getType().equals("CSChat")) {
      task.setData(task.getText());
    } else if (task.getType().equals("CSCreateRoom")) {
      task.setData(task.getTitle());
    }
    return task;
  }

}
