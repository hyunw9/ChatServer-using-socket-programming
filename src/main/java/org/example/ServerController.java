package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.example.Handler.HandlerFunction;
import org.example.Handler.HandlerMap;
import org.example.View.Input;
import org.example.View.Output;

public class ServerController {

  private static final Input input = new Input();
  private static final Output output = new Output();
  private static ThreadPool threadPool;
  private static HandlerMap handlerMap;
  private static Set<SocketChannel> allClients;

  public static void startChatServer() throws IOException, InterruptedException {

    output.askThreadCount();
    allClients = new HashSet<>();
    handlerMap = HandlerMap.addInitialFuncAndCreateMap();
    threadPool = new ThreadPool(input.getInput(), handlerMap);
    for (String s : handlerMap.getHandlerMap().keySet()) {
      System.out.println(s);
    }

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

  private static void acceptSocket(Selector selector, SelectionKey key) throws IOException {
    ServerSocketChannel serverSock = (ServerSocketChannel) key.channel();
    SocketChannel client = serverSock.accept();
    allClients.add(client);
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_READ);
    System.out.format("Accepted: %s%n", client.socket().getRemoteSocketAddress().toString());
    InetSocketAddress userAddr = (InetSocketAddress) client.getRemoteAddress();
    String initialUserName = "(" + userAddr.getHostName()+ " , "+ userAddr.getPort() +")";
    User user = new User(initialUserName);
    output.print(user.getName());
  }

  public static void readSocket(SelectionKey key) throws IOException, InterruptedException {
    ByteBuffer buf = ByteBuffer.allocate(80);
    ByteBuffer buf = ByteBuffer.allocate(65536);

    SocketChannel socketChannel = (SocketChannel) key.channel();
    buf.clear();
    int byteRead = socketChannel.read(buf);

    if (byteRead == -1) {
      throw new InterruptedException();
    } else {
      buf.flip();
      String msg = StandardCharsets.UTF_8.decode(buf).toString();
      System.out.format("Message Received(%d): %s%n", byteRead, msg);
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

