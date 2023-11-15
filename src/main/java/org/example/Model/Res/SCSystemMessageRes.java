package org.example.Model.Res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SCSystemMessageRes implements JsonMessage {

  @JsonProperty
  private String type;

  @JsonProperty
  private String text;

  public SCSystemMessageRes(String text) {
    this.type = "SCSystemMessage";
    this.text = text;
  }

  public String getType() {
    return type;
  }

  public String getText() {
    return text;
  }
}
