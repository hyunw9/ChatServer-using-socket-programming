package org.example;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import org.example.Handler.HandlerMap;
import org.example.Model.MessageTask;
import com.fasterxml.jackson.databind.ObjectMapper;



public class Worker implements Runnable {

  private final Queue<String> taskQueue;
  private final HandlerMap handlerMap;
  private final Lock mutex;

  public Worker(Queue<String> taskQueue, HandlerMap handlerMap, Lock lock) {
    this.taskQueue = taskQueue;
    this.handlerMap = handlerMap;
    this.mutex = lock;
  }

  @Override
  public void run() {
    String jsonTask = null;
    while (true) {
      mutex.lock();

      while (taskQueue.isEmpty()) {
        try {
          taskQueue.wait();
        } catch (InterruptedException e) {
          return;
        }
        jsonTask = taskQueue.poll();
      }
      mutex.unlock();
      try {
        ObjectMapper mapper = new ObjectMapper();
        MessageTask task = mapper.readValue(jsonTask, MessageTask.class);
        handlerMap.getFunction(task.getType()).accept(task.getData());

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}

