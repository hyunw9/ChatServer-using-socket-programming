package org.example;

public class Main {

  public static void main(String[] args) {
    int n =0;
    Thread thread = new Thread(new threadTest(n));
    Thread thread2 = new Thread(new threadTest(n));
    thread.start();
    thread2.start();
  }
}

class threadTest implements Runnable {

  private final int param;

  public threadTest(int param) {
    this.param = param;
  }

  @Override
  public void run() {
    for (int i = 0; i < 1000; i++) {
      System.out.println(param + i);
    }
  }

}
