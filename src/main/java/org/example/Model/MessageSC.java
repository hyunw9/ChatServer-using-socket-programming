package org.example.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageSC {

  @JsonProperty
  private String type;

  @JsonProperty
  private String text;

  public MessageSC(String type, String text) {
    this.type = type;
    this.text = text;
  }
}
