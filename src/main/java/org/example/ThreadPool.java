package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import org.example.Handler.HandlerMap;
import org.example.View.Output;

public class ThreadPool {

  private final static Output output = new Output();
  private final Queue<String> taskQueue = new LinkedList<>();
  private final HandlerMap handlerMap;
  private final List<Worker> workers;
  private final Lock lock;
  private final Condition condition;


  public ThreadPool(String workerCountString, HandlerMap handlerMapArg) throws IOException {
    //initialize
    int workerCount = Integer.parseInt(workerCountString);
    workers = new ArrayList<>();
    lock = new ReentrantLock();
    condition = lock.newCondition();
    handlerMap = handlerMapArg;

    for (int i = 0; i < workerCount; i++) {
      Worker worker = new Worker(taskQueue,handlerMap,lock);
      workers.add(worker);
      output.printCreatedThread(i);
    }
  }

  public void sumbit(String taskMessage){
    taskQueue.add(taskMessage);
    workers.notify();
  }
}
