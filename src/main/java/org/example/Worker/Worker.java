package org.example.Worker;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.example.Handler.HandlerMap;
import org.example.Model.MessageTask;

public class Worker implements Runnable {

  private final Queue<MessageTask> taskQueue;
  private final HandlerMap handlerMap;
  private final Lock mutex;
  private final Condition condition;

  public Worker(Queue<MessageTask> taskQueue, HandlerMap handlerMap, Lock lock,Condition condition) {
    this.taskQueue = taskQueue;
    this.handlerMap = handlerMap;
    this.mutex = lock;
    this.condition = condition;
  }

  @Override
  public void run() {
    while (true) {
      mutex.lock();
      try {
        while (taskQueue.isEmpty()) {
          try {
            condition.await();
          } catch (InterruptedException e) {
            return;
          }
        }

        MessageTask task = taskQueue.poll();
        handlerMap.getFunction(task.getType()).accept(task.getData());

      } catch (Exception e) {
        e.printStackTrace();

        //스레드를 다시 생성할 수도 있지만, 그렇게 된다면 스레드가 스레드풀을 알아야 한다.
        //관리성에서는 용이해지지만 결합도가 증가하는 단점이 있음.
      } finally {
        mutex.unlock();
      }
    }
  }



}

