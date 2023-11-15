package org.example.Model;

import java.util.concurrent.atomic.AtomicInteger;

public class Room {

  private static final AtomicInteger idGenerator = new AtomicInteger(1);

  private int id;
  private String title;

  public Room(String title) {
    if (title.equals("0")) {
      this.id = 0;
    } else {
      this.id = idGenerator.getAndIncrement();
      this.title = title;
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
