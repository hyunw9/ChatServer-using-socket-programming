package org.example.Model.MessageRes.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.nio.ByteBuffer;
import org.example.Model.MessageRes.Serializer.MessageSerializer;

public class SCSystemMessageRes implements MessageRes {

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

  @Override
  public ByteBuffer accept(MessageSerializer serializer) {
    return serializer.serialize(this);
  }
}
