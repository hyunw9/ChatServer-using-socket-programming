package org.example.Worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.example.Handler.HandlerMap;
import org.example.Model.MessageTask;
import org.example.View.Output;
import org.example.Worker.Worker;

public class ThreadPool {

  private final static Output output = new Output();
  private final Queue<MessageTask> taskQueue = new LinkedList<>();
  private final HandlerMap handlerMap;
  private final List<Thread> workerThreads;
  private final Lock lock;
  private final Condition condition;


  public ThreadPool(String workerCountString, HandlerMap handlerMapArg) throws IOException {
    //initialize
    int workerCount = Integer.parseInt(workerCountString);
    workerThreads = new ArrayList<>();
    lock = new ReentrantLock();
    condition = lock.newCondition();
    handlerMap = handlerMapArg;

    for (int i = 0; i < workerCount; i++) {
      Worker worker = new Worker(taskQueue,handlerMap,lock,condition);
      Thread workerThread = new Thread(worker);
      workerThreads.add(workerThread);
    }
  }

  public void start() throws IOException {
    for (Thread workerThread : workerThreads) {
      workerThread.start();
      output.printCreatedThread(workerThreads.indexOf(workerThread));

    }
  }

  public void submit(MessageTask messageTask){
    lock.lock();
    try {
      taskQueue.add(messageTask);
      condition.signal();
    } finally {
      lock.unlock();
    }
  }
}
