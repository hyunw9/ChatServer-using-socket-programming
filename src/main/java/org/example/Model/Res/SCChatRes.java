package org.example.Model.Res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SCChatRes implements JsonMessage{

  @JsonProperty
  private String type;

  @JsonProperty
  private String text;

  @JsonProperty
  private String member;

  public SCChatRes(String text, String member) {
    this.type = "SCChat";
    this.text = text;
    this.member = member;
  }

  public String getType() {
    return type;
  }

  public String getText() {
    return text;
  }

  public String getMember() {
    return member;
  }
}
