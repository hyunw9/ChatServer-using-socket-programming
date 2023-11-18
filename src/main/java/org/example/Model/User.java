package org.example.Model;

import java.nio.channels.SocketChannel;
import org.example.Model.Message.MessageProcessor;

public class User {

  private String name;
  private SocketChannel socketChannel;
  private MessageProcessor processor;



  public User(String name, SocketChannel socketChannel, MessageProcessor processor) {
    this.name = name;
    this.socketChannel = socketChannel;
    this.processor = processor;


  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SocketChannel getSocketChannel() {
    return socketChannel;
  }

}
