package org.example.Model;

public class MessageTask {

  private String type;
  private String data;

  public MessageTask(String type, String data) {
    this.type = type;
    this.data = data;
  }

  public String getType() {
    return type;
  }

  public String getData() {
    return data;
  }
}
