package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import org.example.Handler.HandlerMap;
import org.example.Model.ChatRoomManager;
import org.example.Model.MessageTask;
import org.example.Model.Room;
import org.example.Model.User;
import org.example.View.Input;
import org.example.View.Output;
import org.example.Worker.ThreadPool;

public class ServerController {

  private static final Input input = new Input();
  private static final Output output = new Output();
  private static ThreadPool threadPool;
  private static HandlerMap handlerMap;
  private static Room chatRoom;
  private static ChatRoomManager chatRoomManager;


  public static void startChatServer() throws IOException, InterruptedException {

    output.askThreadCount();
    chatRoomManager = new ChatRoomManager();
    handlerMap = HandlerMap.addInitialFuncAndCreateMap(chatRoomManager);
    threadPool = new ThreadPool(input.getInput(), handlerMap);
    threadPool.start();


    while (true) {
      Selector selector = Selector.open();

      ServerSocketChannel server = ServerSocketChannel.open();
      InetSocketAddress socketAddress = new InetSocketAddress(9142);
      server.bind(socketAddress);

      output.printServerOpenedPort(socketAddress);

      server.configureBlocking(false);

      server.register(selector, SelectionKey.OP_ACCEPT);

      while (true) {
        selector.select();

        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
          SelectionKey key = keyIterator.next();
          keyIterator.remove();

          if (!key.isValid()) {
            continue;
          }

          if (key.isAcceptable()) {
            acceptSocket(selector, key);
          } else if (key.isReadable()) {
            readSocket(key);
          }
        }
      }
    }
  }

  private static void initMessageProcessor() throws IOException {
    output.print("메세지 타입을 입력하세요. json or protobuf: ");
    String format = input.getInput();
    lock = new ReentrantLock();
    if (format.equals("protobuf")){

      messageProcessor = new ProtobufMessageProcessor();
      serializer = new ProtobufSerializer();
      chatRoomManager = ChatRoomManager.getInstance();
      chatRoomManager.setSerializer(serializer);
      userManager = UserManager.createUserManager(serializer);

      output.print("protobuf를 처리하는 서버 생성");
    }else if(format.equals("json")){
      messageProcessor = new JsonMessageProcessor();
      serializer = new JsonSerializer();
      chatRoomManager = ChatRoomManager.getInstance();
      chatRoomManager.setSerializer(serializer);
      userManager = UserManager.createUserManager(serializer);

      output.print("JSON 처리하는 서버 생성");

    }else{
      throw new IllegalArgumentException("알 수 없는 메세지 형식 : "+ format);
    }
  }

  private static void initThreadPool() throws IOException {
    output.askThreadCount();
    chatRoomManager.setLock(lock);
    handlerMap = HandlerMap.addInitialFuncAndCreateMap(chatRoomManager, userManager);
    threadPool = new ThreadPool(input.getInput(), handlerMap);
  }

  private static void acceptSocket(Selector selector, SelectionKey key) throws IOException {
    ServerSocketChannel serverSock = (ServerSocketChannel) key.channel();
    SocketChannel client = serverSock.accept();
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_READ);
    InetSocketAddress userAddr = (InetSocketAddress) client.getRemoteAddress();
    String initialUserName = "(" + userAddr.getHostName()+ " , "+ userAddr.getPort() +")";
    User user = new User(initialUserName);
    output.print(user.getName());
  }

  public static void readSocket(SelectionKey key) throws IOException, InterruptedException {
    ByteBuffer buf = ByteBuffer.allocate(65536);

    SocketChannel socketChannel = (SocketChannel) key.channel();
    buf.clear();
    int byteRead = socketChannel.read(buf);

    if (byteRead == -1) {
      throw new InterruptedException();
    } else {
      buf.flip();
      String msg = StandardCharsets.UTF_8.decode(buf).toString();
      msg = msg.substring(msg.indexOf("{"), msg.lastIndexOf("}") + 1);
      System.out.println(msg);

      ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      MessageTask task = mapper.readValue(msg, MessageTask.class);

      if (task.getType().equals("CSName")) {
        task.setData(task.getName());
      } else if (task.getType().equals("CSJoinRoom")) {
        task.setData(task.getRoomId());
      } else if (task.getType().equals("CSChat")) {
        task.setData(task.getText());
      }else if(task.getType().equals("CSCreateRoom")){
        task.setData(task.getTitle());
      }
      System.out.println(task.getData());

      threadPool.submit(task);
      if (msg.equals("Done")) {
        throw new InterruptedException();
      }
    }
  }
}

