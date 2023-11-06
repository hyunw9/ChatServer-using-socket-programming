package org.example.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {

  private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

  public String getInput() throws IOException {
    return input.readLine();
  }
}
