package org.example.View;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Output {

  private final BufferedWriter output;
  private final StringBuilder builder;

  public Output() {
    this.output = new BufferedWriter(new OutputStreamWriter(System.out));
    this.builder = new StringBuilder();
  }

  public void askThreadCount() throws IOException {
    output.write("몇개의 스레드를 생성할까요 ?: ");
    output.flush();
  }

  public void print(int s) throws IOException {
    output.write("메세지 작업 스레드 #" +s+"생성\n");
    output.flush();
  }
}

