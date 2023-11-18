package org.example.Worker;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.example.Handler.HandlerMap;
import org.example.Model.Message.MessageTask;

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
            //lock 획득 시 까지 wait
            condition.await();
          } catch (InterruptedException e) {
            return;
          }
        }
        //lock 획득이 가능하다면 wake 후 queue에서 task를 가져온 후 , handler에 넘겨 처리합니다.
        System.out.println("thread wake up");
        MessageTask task = taskQueue.poll();
        handlerMap.getFunction(task.getType()).accept(task);

      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        mutex.unlock();
      }
    }
  }
}

