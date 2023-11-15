package org.example.Model;

import java.nio.channels.SocketChannel;

public class User {

  private String name;
  private SocketChannel socketChannel;

  public User(String name, SocketChannel socketChannel) {
    this.name = name;
    this.socketChannel = socketChannel;
  }

  public String getName() {
    return name;
  }

  public SocketChannel getSocketChannel() {
    return socketChannel;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSocketChannel(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }
}
