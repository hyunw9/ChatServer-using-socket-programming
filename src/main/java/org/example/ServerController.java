package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.example.Handler.HandlerMap;
import org.example.Model.Manager.ChatRoomManager;
import org.example.Model.Message.Json.JsonMessageProcessor;
import org.example.Model.Message.MessageProcessor;
import org.example.Model.Message.MessageTask;
import org.example.Model.Message.Protobuf.ProtobufMessageProcessor;
import org.example.Model.MessageRes.Serializer.JsonSerializer;
import org.example.Model.MessageRes.Serializer.MessageSerializer;
import org.example.Model.MessageRes.Serializer.ProtobufSerializer;
import org.example.Model.User;
import org.example.Model.Manager.UserManager;
import org.example.View.Input;
import org.example.View.Output;
import org.example.Worker.ThreadPool;

public class ServerController {

  private static MessageProcessor messageProcessor;

  private static final Input input = new Input();
  public static final Output output = new Output();

  private static ThreadPool threadPool;
  private static HandlerMap handlerMap;
  private static ChatRoomManager chatRoomManager;
  private static UserManager userManager;
  private static Lock lock;

  private static Selector selector;
  private static MessageSerializer serializer;

  public static void startChatServer() throws IOException {

    initMessageProcessor();

    initThreadPool();

    threadPool.start();

    while (true) {
      selector = Selector.open();

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

    //유저가 연결을 시도하면, IP, Port로 유저의 이름 지정 후 UserManager에 추가합니다.
    String initialUserName = getInitialUserNameBuilder(userAddr);
    User user = new User(initialUserName,client,messageProcessor);
    userManager.addUser(user);

    output.print("새로운 유저 접속: " + user.getName());
  }


  public static void readSocket(SelectionKey key) throws IOException {
    SocketChannel socketChannel = (SocketChannel)key.channel();

    //읽기 가능한 상태가 된다면, messageProcessor의 구현체가 메세지를 읽어서 처리합니다.
    MessageTask task = messageProcessor.processMessage(socketChannel);
    if(task.getType().equals("CSShutdown")){
      shutdownServer();
    }else {
      //threadPool에 Task를 제출합니다.
      threadPool.submit(task);
    }
  }

